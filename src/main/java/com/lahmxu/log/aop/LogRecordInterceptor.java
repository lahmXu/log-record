package com.lahmxu.log.aop;

import com.lahmxu.biz.ILogRecordService;
import com.lahmxu.biz.IOperatorGetService;
import com.lahmxu.log.aop.LogRecordOperationSource;
import com.lahmxu.log.context.LogRecordContext;
import com.lahmxu.log.function.IFunctionService;
import com.lahmxu.log.pojo.LogRecordOps;
import com.lahmxu.log.pojo.MethodExecuteResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.aop.support.AopUtils.getTargetClass;

@Slf4j
@Data
public class LogRecordInterceptor implements MethodInterceptor {

    private LogRecordOperationSource logRecordOperationSource;

    private String tenant;

    private IFunctionService functionService;

    private ILogRecordService logRecordService;

    private IOperatorGetService operatorGetService;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        // 记录日志
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = getTargetClass(target);
        Object result = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        LogRecordContext.putEmptySpan();
        Collection<LogRecordOps> operations = new ArrayList<>();
        Map<String, String> functionNameAndReturnMap = new HashMap<>();
        try {
            operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
            //业务逻辑执行前的自定义函数解析
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
        } catch (Exception e) {
            log.error("log record parse before function exception", e);
        }
        try {
            result = invoker.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }
        try {
            if (!CollectionUtils.isEmpty(operations)) {
                recordExecute(result, method, args, operations, targetClass,
                        methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
            }
        } catch (Exception t) {
            //记录日志错误不要影响业务
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
        return null;
    }

    private Map<String, String> processBeforeExecuteFunctionTemplate(List<String> spElTemplates, Class<?> targetClass, Method method, Object[] args) {
        // TODO 执行自定义函数
        return null;
    }

    private void recordExecute(Object result, Method method, Object[] args, Collection<LogRecordOps> operations, Class<?> targetClass, boolean methodExecuteResultSuccess, String errorMsg, Map<String, String> functionNameAndReturnMap) {
        // TODO 执行日志拼接
    }
}
