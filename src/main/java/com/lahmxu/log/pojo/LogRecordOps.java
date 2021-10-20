package com.lahmxu.log.pojo;

import lombok.Data;

@Data
public class LogRecordOps {

    /**
     * 操作日志的文本模板
     */
    private String content;

    /**
     * 操作日失败的文本模板
     */
    private String fail;

    /**
     * 操作日志的执行人
     */
    private String operator;

    /**
     * 操作日志绑定的业务对象标识
     */
    private String bizNo;

    /**
     * 操作日志的种类
     */
    private String category;

    /**
     * 扩展参数, 记录操作日志的修改详情
     */
    private String detail;

    /**
     * 记录日志的条件
     */
    private String condition;

}
