package com.roily.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class User {
    private int age;
    private String name;
    private String paw;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaw() {
        return paw;
    }

    public void setPaw(String paw) {
        this.paw = paw;
    }

    public User(int age, String name, String paw) {
        this.age = age;
        this.name = name;
        this.paw = paw;
    }

    public User() {
    }
}
