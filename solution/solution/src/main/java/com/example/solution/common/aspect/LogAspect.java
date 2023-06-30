package com.example.solution.common.aspect;

import com.example.solution.common.aspect.annotation.LogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 记录日志
     */
    @Before("@annotation(logAnnotation)")
    private void logBefore(JoinPoint joinPoint, LogAnnotation logAnnotation) {
        log.info(logAnnotation.value());
    }

    @After("@annotation(com.example.solution.common.aspect.annotation.LogAnnotation)")
    private void logAfter(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
        LogAnnotation annotation = AnnotationUtils.findAnnotation(methodSignature.getMethod(), LogAnnotation.class);
//        AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);

        log.info(annotation.value());
    }

}
