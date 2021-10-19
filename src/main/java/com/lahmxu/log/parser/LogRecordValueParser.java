package com.lahmxu.log.parser;

import com.lahmxu.log.function.IFunctionService;
import org.springframework.expression.EvaluationContext;

/**
 * 日志内容转换类
 */
public class LogRecordValueParser {

    private LogRecordExpressionEvaluator logRecordExpressionEvaluator;

    private IFunctionService functionService;

    private LogRecordExpressionEvaluator expressionEvaluator;

    // TODO 不知道怎么实现
//    EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);



}
