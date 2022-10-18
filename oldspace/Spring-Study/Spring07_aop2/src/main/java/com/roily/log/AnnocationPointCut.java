package com.roily.log;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author Roly_Fish
 */
@Component
@Aspect
public class AnnocationPointCut {
    @Before("execution(* com.roily.service.UserServiceImpl.*(..))")
    public void before() {
        System.out.println("方法执行前==");
    }

    @After("execution(* com.roily.service.UserServiceImpl.*(..))")
    public void after() {
        System.out.println("方法执行后==");
    }
}
