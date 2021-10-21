package com.lahmxu.log.context;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    /**
     * 保存变量信息到 stack 中
     * @param name
     * @param value
     */
    public static void putVariable(String name, Object value){
        if (variableMapStack.get() == null){
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack.size() == 0){
            variableMapStack.get().push(new HashMap<>());
        }
        variableMapStack.get().peek().put(name, value);
    }

    public static Object getVariable(String key){
        Map<String, Object> variableMap = variableMapStack.get().peek();
        return variableMap.get(key);

    }

    public static  Map<String, Object> getVariables(){
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        return mapStack.peek();
    }

    public static void clear(){
        if (variableMapStack.get() != null){
            variableMapStack.get().pop();
        }
    }

    /**
     * 日志适用方不需要使用到这个方法
     * 每进入一个方法初始化一个 span 放入到 stack 中,方法执行完 pop 掉这个 span
     */
    public static void putEmptySpan(){
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack == null){
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        variableMapStack.get().push(Maps.newHashMap());
    }
}
