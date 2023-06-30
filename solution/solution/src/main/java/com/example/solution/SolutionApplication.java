package com.example.solution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true) // 表示通过aop框架暴露该代理对象,AopContext能够访问
@SpringBootApplication
public class SolutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolutionApplication.class, args);
    }

}
