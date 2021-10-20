package com.lahmxu.biz.service;

import com.lahmxu.log.aop.BeanFactoryLogRecordAdvisor;

import java.util.logging.LogRecord;

/**
 * 拼接后的信息日志存储
 */
public interface ILogRecordService {

    void record(LogRecord logRecord);
}
