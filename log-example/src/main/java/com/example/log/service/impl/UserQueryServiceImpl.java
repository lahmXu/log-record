package com.example.log.service.impl;

import com.example.log.pojo.LogRecordType;
import com.example.log.service.UserQueryService;
import com.lahmxu.log.context.LogRecordContext;
import com.lahmxu.log.starter.annotation.LogRecord;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Override
    @LogRecord(success = "获取用户列表,内层方法调用人{{#user}}", prefix = LogRecordType.ORDER, bizNo = "{{#user}}")
    public List<User> getUserList(List<String> userIds) {
        LogRecordContext.putVariable("user", "mzt");
        return null;
    }
}
