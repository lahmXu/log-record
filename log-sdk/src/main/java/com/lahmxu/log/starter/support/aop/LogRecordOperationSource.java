package com.lahmxu.log.starter.support.aop;

import com.lahmxu.log.pojo.LogRecordOps;
import com.lahmxu.log.starter.annotation.LogRecord;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

/**
 * LogRecord 注解操作
 */
public class LogRecordOperationSource {


    public Collection<LogRecordOps> computeLogRecordOperations(Method method, Class<?> targetClass) {
        // Don't allow no-public methods as required.
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        return parseLogRecords(specificMethod);
    }

    private Collection<LogRecordOps> parseLogRecords(AnnotatedElement annotatedElement){
        Collection<LogRecord> logRecords = AnnotatedElementUtils.getAllMergedAnnotations(annotatedElement, LogRecord.class);
        Collection<LogRecordOps> result = null;
        if (!CollectionUtils.isEmpty(logRecords)){
            result = lazyInit(result);
            for (LogRecord logRecord : logRecords) {
                result.add(parseLogRecord(annotatedElement, logRecord));
            }
        }
        return result;
    }

    private LogRecordOps parseLogRecord(AnnotatedElement annotatedElement, LogRecord logRecord){
        LogRecordOps logRecordOps = LogRecordOps.builder()
                .successLogTemplate(logRecord.success())
                .failLogTemplate(logRecord.fail())
                .bizKey(logRecord.prefix().concat("_").concat(logRecord.bizNo()))
                .bizNo(logRecord.bizNo())
                .operatorId(logRecord.operator())
                .category(StringUtils.isEmpty(logRecord.category()) ? logRecord.prefix() : logRecord.category())
                .detail(logRecord.detail())
                .condition(logRecord.condition())
                .build();
        validateLogRecordOperation(annotatedElement, logRecordOps);
        return logRecordOps;
    }

    private void validateLogRecordOperation(AnnotatedElement ae, LogRecordOps recordOps) {
        if (!StringUtils.hasText(recordOps.getSuccessLogTemplate()) && !StringUtils.hasText(recordOps.getFailLogTemplate())) {
            throw new IllegalStateException("Invalid logRecord annotation configuration on '" +
                    ae.toString() + "'. 'one of successTemplate and failLogTemplate' attribute must be set.");
        }
    }

    private Collection<LogRecordOps> lazyInit(Collection<LogRecordOps> ops){
        return (ops != null ? ops : new ArrayList<>(1));
    }


}
