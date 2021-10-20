package com.lahmxu.biz.service.impl;

import com.lahmxu.biz.service.IOperatorGetService;
import com.lahmxu.biz.pojo.Operator;

public class DefaultOperatorGetServiceImpl implements IOperatorGetService {
    @Override
    public Operator getUser() {
        //UserUtils 是获取用户上下文的方法
        return new Operator("张三三");
    }
}
