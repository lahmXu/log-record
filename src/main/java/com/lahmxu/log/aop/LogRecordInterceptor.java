package com.lahmxu.log.aop;

import com.lahmxu.biz.service.ILogRecordService;
import com.lahmxu.biz.service.IOperatorGetService;
import com.lahmxu.log.context.LogRecordContext;
import com.lahmxu.log.function.IFunctionService;
import com.lahmxu.log.parser.LogRecordValueParser;
import com.lahmxu.log.pojo.LogRecordOps;
import com.lahmxu.log.pojo.MethodExecuteResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.util.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * 切面
 */
@Slf4j
@Data
public class LogRecordInterceptor implements MethodInterceptor {

    private LogRecordOperationSource logRecordOperationSource;

    private String tenant;

    private IFunctionService functionService;

    private ILogRecordService logRecordService;

    private IOperatorGetService operatorGetService;

    private LogRecordValueParser logRecordValueParser;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        // 记录日志
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
        // TODO 获取执行模板

//        List<String> spElTemplates;
//
//        String operatorId = "";
//
//        String realOperatorId = "";
//        if (StringUtils.isEmpty(operatorId)) {
//            if (operatorGetService.getUser() == null || StringUtils.isEmpty(operatorGetService.getUser().getOperatorId())) {
//                throw new IllegalArgumentException("user is null");
//            }
//            realOperatorId = operatorGetService.getUser().getOperatorId();
//        } else {
//            spElTemplates = Lists.newArrayList(bizKey, bizNo, action, operatorId, detail);
//        }

        return new ArrayList<>();
    }

    private Map<String, String> processBeforeExecuteFunctionTemplate(List<String> spElTemplates, Class<?> targetClass, Method method, Object[] args) {
        // TODO 执行自定义函数

        return null;
    }

    private void recordExecute(Object result, Method method, Object[] args, Collection<LogRecordOps> operations, Class<?> targetClass, boolean methodExecuteResultSuccess, String errorMsg, Map<String, String> functionNameAndReturnMap) {
        // TODO 执行日志拼接
    }
}
