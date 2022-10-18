package com.lin.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//等价于 <bean id="user" class="com.lin.pojo.User"/>
//@Component 组件
@Component
@Scope("singleton")
public class User {
    //等价于 <property name="name" value="lin"/>
    @Value("lin")
    public String name;
}
