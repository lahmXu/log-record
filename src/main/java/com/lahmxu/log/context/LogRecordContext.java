package com.lahmxu.log.context;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    protected void setVariables(String key, Object obj){
        // TODO 存储变量信息到 map 中
//        variableMapStack.get().
    };

    public static  Map<String, Object> getVariables(){
        return variableMapStack.get().peek();
    }

    public static void putEmptySpan(){
        variableMapStack.get().push(new HashMap<>());
    }

    public static void clear(){
        variableMapStack.get().pop();
    }
}
