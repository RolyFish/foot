package com.roily.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//<bean id="user" class="XX.XX.XX"/>
@Component
public class User {
    @Value("于延闯")
    public String name;


    public void say(){
        System.out.println("@Component注解装配bean");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
