package com.example.log.service.impl;

import com.example.log.pojo.Order;
import com.example.log.service.OrderQueryService;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    @Override
    public Order queryOrder(long parseLong) {
        Order order = new Order();
        order.setProductName("xxxx");
        return order;
    }
}
