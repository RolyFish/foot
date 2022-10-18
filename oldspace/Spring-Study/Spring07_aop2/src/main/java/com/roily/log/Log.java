package com.roily.log;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * 前置
 * @author Roly_Fish
 */
public class Log implements MethodBeforeAdvice {

    public void before(Method method, Object[] args, Object target) throws Throwable {

        System.out.println(target.getClass().getName()+"的"+method.getName()+"执行");
    }
}
