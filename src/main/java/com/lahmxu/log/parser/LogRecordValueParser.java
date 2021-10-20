package com.lahmxu.log.parser;

import com.lahmxu.log.annotation.LogRecord;
import com.lahmxu.log.function.IFunctionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志内容转换类
 */
@Component
public class LogRecordValueParser {

    private IFunctionService functionService;

    private LogRecordExpressionEvaluator expressionEvaluator;

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);


}
