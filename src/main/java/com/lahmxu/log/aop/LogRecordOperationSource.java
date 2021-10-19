package com.lahmxu.log.aop;

import com.lahmxu.log.pojo.LogRecordOps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * LogRecord 注解操作
 */
public class LogRecordOperationSource {

    /**
     * 解析 methods 上是有 {@link com.lahmxu.log.annotation.LogRecord} 注解, 解析出各个参数
     * @param method
     * @param targetClass
     * @return
     */
    public List<LogRecordOps> computeLogRecordOperations(Method method, Class<?> targetClass){

        // TODO 拦截 @LogRecord 注解的方法,并解析参数
        return null;
    };
}
