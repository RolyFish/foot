package com.roily.Test2;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/9
 */
public class TestInterfaceFactorybean implements FactoryBean<TestInterface>, InitializingBean , ApplicationListener<ApplicationEvent> {


    int a;

    public void setA(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }

    @Override
    public TestInterface getObject() throws Exception {
        System.out.println("getObject");
        return new TestInterfaceFactory();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("after");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("onApplicationEvent");
    }
}
