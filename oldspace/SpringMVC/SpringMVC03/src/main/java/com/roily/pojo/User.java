package com.roily.pojo;


import lombok.Data;

@Data
public class User {
    private String name;
    private int age;
    private String sex;

    public User(String name, int age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
