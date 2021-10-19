package com.lahmxu.log.aop;

import com.lahmxu.log.context.LogRecordContext;
import com.lahmxu.log.pojo.LogRecordOps;
import com.lahmxu.log.pojo.MethodExecuteResult;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * 自定义切点
 */
public class BeanFactoryLogRecordAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private LogRecordOperationSource logRecordOperationSource;

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

    /**
     * 通过扫描@Re
     * @return
     */
    @Override
    public Pointcut getPointcut() {
        return new LogRecordPointcut();
    }
}
