package com.lahmxu.biz.service;

import com.lahmxu.biz.pojo.Operator;

/**
 * 获取操作者接口
 */
public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如 UserContext.getCurrentUser()
     *
     * @return 转换成Operator返回
     */
    Operator getUser();
}