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

    };

    public static  Map<String, Object> getVariables(){
        return new HashMap<>();
    }

    public static void putEmptySpan(){

    }

    public static void clear(){

    }
}
