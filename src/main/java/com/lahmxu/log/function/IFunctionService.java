package com.lahmxu.log.function;

import com.lahmxu.log.parser.IParseFunction;

/**
 * 自定义函数接口
 */
public interface IFunctionService {

    /**
     * 根据函数名称调用函数
     * @param functionName
     * @param value
     * @return
     */
    String apply(String functionName, String value);

    boolean beforeFunction(String functionName);

}
