package com.example.log.spel;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * 测试 SpEL  参考链接: https://www.jianshu.com/p/e0b50053b5d3
 * SpEL 有三种用法:
 * 1. 在注解@Value中使用
 * 2. XML 配置
 * 3. 在代码块中使用 Expression
 */
public class SpELTest {

    /**
     * 计算表达式
     */
    @Test
    public void TestExpression1() {

        // 创建 ExpressionParser 解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        // 表达式放置
        Expression expression = parser.parseExpression("'Hello World'.concat('!')");
        // 执行表达式, 默认容器是 spring 本身的容器: Application
        Object value = expression.getValue();

        System.out.println(value);
    }

    /**
     * 获取容器内的变量
     */
    @Test
    public void TestExpression2() {

        // 创建 ExpressionParser 解析表达式
        ExpressionParser parser = new SpelExpressionParser();

        // 表达式放置
        Expression expression = parser.parseExpression("{#name}");
        Expression expression2= parser.parseExpression("{#root.name}");

        // 创建一个虚拟的容器 EvaluationContext
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        // 向容器内添加 bean
        BeanA beanA = new BeanA();

        // 直接设置的参数可以不加前缀
        ctx.setVariable("beanName", beanA);
        ctx.setVariable("name", "李四");

        // setRootObject 并非必须;
        // 一个 EvaluationContext 只能有一个 RootObject, 引用它的属性时,可以需要加前缀;
        BeanA rootObject = new BeanA();
        rootObject.setName("张三");
        ctx.setRootObject(rootObject);

        // getValue 有参数 ctx, 从新容器中根据 SpEL 表达式获取所需的值
        Object value2 = expression.getValue(ctx);
        System.out.println(value2);
        Object value3 = expression2.getValue(ctx);
        System.out.println(value3);
    }

    /**
     * 方法调用
     */
    @Test
    public void test(){
        ExpressionParser parser = new SpelExpressionParser();

        // 创建一个虚拟的容器 EvaluationContext
        StandardEvaluationContext ctx = new StandardEvaluationContext();

        // 添加到容器
        String s = new String("abcdefg");

        // 直接设置的参数可以不加前缀
        ctx.setVariable("str", s);

        String result = parser.parseExpression("#str.substring(0,1)").getValue(ctx, String.class);

        System.out.println(result);
    }

    /**
     * 注册自定义函数
     */
    @Test
    public void testCuntome() throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();

        StandardEvaluationContext context = new StandardEvaluationContext();

        Method parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);

        context.registerFunction("parseIntTest", parseInt);

        Integer value = parser.parseExpression("#parseIntTest(100)").getValue(context, Integer.class);

        System.out.println(value);
    }
}
