package com.roily.pojo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Roly_Fish
 */
@Component
public class User2 {
    //@Value("于延闯2")
    private String name;

    public User2() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

