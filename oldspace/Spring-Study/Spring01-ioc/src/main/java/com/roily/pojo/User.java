package com.roily.pojo;

/**
 * @author Roly_Fish
 */
public class User {
    private String name;
    private String pwd;

    public User() {
        System.out.println("无参构造器");
    }

    public User(String name) {
        this.name = name;
        System.out.println("有参构造器" + name);
    }

    public User(String name, String pwd) {
        this.pwd = pwd;
        System.out.println("有参构造器" + name + " " + pwd);
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("Hello," + name);
    }


    @Override
    public boolean equals(Object obj) {
        String name = null;
        String name2 = null;
        try {
            name = this.getClass().getDeclaredConstructor().getName();
            name2 = obj.getClass().getDeclaredConstructor().getName();
            System.out.println(name);
            System.out.println(name2);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return name.equals(name2);
    }
}
