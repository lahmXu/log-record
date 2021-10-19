package com.lahmxu.log.annotation;

import com.lahmxu.log.config.LogRecordConfigureSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 用于开关日志记录的功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogRecordConfigureSelector.class)
public @interface EnableLogRecord {

    String tenant();

    AdviceMode mode() default AdviceMode.PROXY;

}
