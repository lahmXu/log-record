package com.lahmxu.log.service.impl;

import com.lahmxu.log.pojo.LogRecord;
import com.lahmxu.log.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService {
    @Override
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", logRecord);
    }
}
