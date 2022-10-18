package com.roily.pojo;

public class User {
    private  String name;
    private  int age;

    public User() {
        System.out.println("无参");
    }

    public User(String name, int age) {
        System.out.println("有参");
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
