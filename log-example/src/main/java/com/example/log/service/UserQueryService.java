package com.example.log.service;

import org.apache.catalina.User;

import java.util.List;

public interface UserQueryService {
    List<User> getUserList(List<String> userIds);
}
