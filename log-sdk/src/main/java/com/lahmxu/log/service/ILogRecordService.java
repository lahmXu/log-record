package com.lahmxu.log.service;

import com.lahmxu.log.pojo.LogRecord;


/**
 * 拼接后的信息日志存储
 */
public interface ILogRecordService {

    void record(LogRecord logRecord);
}
