package com.lahmxu.log.service.impl;

import com.lahmxu.log.service.IOperatorGetService;
import com.lahmxu.log.pojo.Operator;

public class DefaultOperatorGetServiceImpl implements IOperatorGetService {
    @Override
    public Operator getUser() {
        //UserUtils 是获取用户上下文的方法
        return new Operator("张三三");
    }
}
