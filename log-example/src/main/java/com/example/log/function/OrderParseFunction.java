package com.example.log.function;

import com.example.log.pojo.Order;
import com.example.log.service.OrderQueryService;
import com.lahmxu.log.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@Component
public class OrderParseFunction implements IParseFunction {
    @Resource
    @Lazy
    private OrderQueryService orderQueryService;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "ORDER";
    }

    @Override
    public String apply(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        log.info("###########,{}", value);
        Order order = orderQueryService.queryOrder(Long.parseLong(value));
        return order.getProductName().concat("(").concat(value).concat(")");
    }
}
