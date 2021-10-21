package com.lahmxu.log.service;

public interface IParseFunction {

    /**
     * 表示自定义函数是否在业务代码执行之前解析
     * @return
     */
    default boolean executeBefore(){
        return false;
    }

    String functionName();

    String apply(String value);
}
