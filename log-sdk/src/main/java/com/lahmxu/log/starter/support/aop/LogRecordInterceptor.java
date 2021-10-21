package com.lahmxu.log.starter.support.aop;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.lahmxu.log.pojo.LogRecord;
import com.lahmxu.log.service.ILogRecordService;
import com.lahmxu.log.service.IOperatorGetService;
import com.lahmxu.log.context.LogRecordContext;
import com.lahmxu.log.starter.support.parser.LogRecordValueParser;
import com.lahmxu.log.pojo.LogRecordOps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 切面
 */
@Slf4j
public class LogRecordInterceptor extends LogRecordValueParser implements MethodInterceptor, InitializingBean {

    private LogRecordOperationSource logRecordOperationSource;

    private String tenantId;

    private IOperatorGetService operatorGetService;

    private ILogRecordService logRecordService;

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setOperatorGetService(IOperatorGetService operatorGetService) {
        this.operatorGetService = operatorGetService;
    }

    public void setLogRecordService(ILogRecordService logRecordService) {
        this.logRecordService = logRecordService;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = getTargetClass(target);

        // 初始化成员变量
        Object result = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        LogRecordContext.putEmptySpan();
        Collection<LogRecordOps> operations = new ArrayList<>();
        Map<String, String> functionNameAndReturnMap = new HashMap<>();

        // 解析注解并执行注解中的函数
        try {
            operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
        } catch (Exception e) {
            log.error("log record parse before function exception", e);
        }

        // 执行注解实际方法
        try {
            result = invoker.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }

        // 组装执行结果
        try {
            if (!CollectionUtils.isEmpty(operations)) {
                recordExecute(result, method, args, operations, targetClass, methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
            }
        } catch (Exception t) {
            log.error("log record parse exception", t);
        } finally {
            LogRecordContext.clear();
        }

        if (methodExecuteResult.throwable != null) {
            throw methodExecuteResult.throwable;
        }
        return result;
    }

    private List<String> getBeforeExecuteFunctionTemplate(Collection<LogRecordOps> operations) {
        List<String> spElTemplates = new ArrayList<>();
        for (LogRecordOps operation : operations) {
            List<String> templates = getSpELTemplates(operation, operation.getSuccessLogTemplate());
            if (!CollectionUtils.isEmpty(templates)) {
                spElTemplates.addAll(templates);
            }
        }
        return spElTemplates;
    }

    private List<String> getSpELTemplates(LogRecordOps operation, String action) {
        List<String> spELTemplates = Lists.newArrayList(operation.getBizKey(), operation.getBizNo(), action, operation.getDetail());
        if (!StringUtils.isEmpty(operation.getCondition())) {
            spELTemplates.add(operation.getCondition());
        }
        return spELTemplates;
    }


    private void recordExecute(Object result, Method method, Object[] args, Collection<LogRecordOps> operations, Class<?> targetClass, boolean success, String errorMsg, Map<String, String> functionNameAndReturnMap) {
        for (LogRecordOps operation : operations) {
            String action = getActionContent(success, operation);
            if (StringUtils.isEmpty(action)){
                continue;
            }
            List<String> spELTemplate = getSpELTemplates(operation, action);
            String operatorIdFromService = getOperatorIdFromServiceAndPutTemplate(operation, spELTemplate);

            Map<String, String> expressionValues = processTemplate(spELTemplate, result, targetClass, method, args, errorMsg, functionNameAndReturnMap);
            if (logConditionPassed(operation.getCondition(), expressionValues)) {
                LogRecord logRecord = LogRecord.builder()
                        .tenant(tenantId)
                        .bizKey(expressionValues.get(operation.getBizKey()))
                        .bizNo(expressionValues.get(operation.getBizNo()))
                        .operator(getRealOperatorId(operation, operatorIdFromService, expressionValues))
                        .category(operation.getCategory())
                        .detail(expressionValues.get(operation.getDetail()))
                        .action(expressionValues.get(action))
                        .createTime(new Date())
                        .build();

                // action 为空, 不记录日志
                if (StringUtils.isEmpty(logRecord.getAction())){
                    continue;
                }

                // save log 需要新开事务, 失败日志不能因为事务回滚而丢失
                Preconditions.checkNotNull(logRecordService, "LogRecordService not null");
                logRecordService.record(logRecord);
            }
        }
    }

    private boolean logConditionPassed(String condition, Map<String, String> expressionValues) {
        return StringUtils.isEmpty(condition) || StringUtils.endsWithIgnoreCase(expressionValues.get(condition), "true");
    }

    private String getRealOperatorId(LogRecordOps operation, String operatorIdFromService, Map<String, String> expressionValues) {
        return !StringUtils.isEmpty(operatorIdFromService) ? operatorIdFromService : expressionValues.get(operation.getOperatorId());
    }

    private String getOperatorIdFromServiceAndPutTemplate(LogRecordOps operation, List<String> spELTemplates) {
        String realOperatorId = "";
        if (StringUtils.isEmpty(operation.getOperatorId())) {
            realOperatorId = operatorGetService.getUser().getOperatorId();
            if (StringUtils.isEmpty(realOperatorId)) {
                throw new IllegalArgumentException("[LogRecord] operator is null");
            }
        } else {
            spELTemplates.add(operation.getOperatorId());
        }
        return realOperatorId;
    }


    private String getActionContent(boolean success, LogRecordOps operation) {
        String action = "";
        if (success) {
            action = operation.getSuccessLogTemplate();
        } else {
            action = operation.getFailLogTemplate();
        }
        return action;
    }

    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        logRecordService = beanFactory.getBean(ILogRecordService.class);
        operatorGetService = beanFactory.getBean(IOperatorGetService.class);
        Preconditions.checkNotNull(logRecordService, "LogRecordSevice not null");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class MethodExecuteResult {

        private boolean success;

        private Throwable throwable;

        private String errorMsg;
    }
}
