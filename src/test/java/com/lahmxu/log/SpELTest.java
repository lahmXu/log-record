package com.lahmxu.log;

import com.lahmxu.biz.pojo.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * 测试 SpEL
 */
public class SpELTest {
    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("#root.purchaseName");

        // 创建 rootObject, 用于存储数据
        Order order = new Order();
        order.setPurchaseName("张三");

        System.out.println(expression.getValue(order));
    }
}
