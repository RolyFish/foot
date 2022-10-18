package com.roily.Test2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/9
 */
public class cli {
    public static void main(String[] args) {
        ApplicationContext contextontext = new ClassPathXmlApplicationContext("test.xml");

        Object testInterfaceFactorybean = contextontext.getBean("TestInterfaceFactorybean");

        System.out.println(testInterfaceFactorybean.getClass().getName());
    }
}
