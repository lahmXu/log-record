package com.lahmxu.biz.impl;

import com.lahmxu.biz.ILogRecordService;
import com.lahmxu.log.aop.BeanFactoryLogRecordAdvisor;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.LogRecord;

@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService {
    @Override
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", logRecord);
    }
}
