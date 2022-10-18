package com.lin.demo02;

//真实对象
public class UserServiceImpl implements UserService{
    //1.改动原有的业务（Service）代码，在公司中是大忌
    //开闭原则
    public void add() {
        System.out.println("增加");
    }

    public void delete() {
        System.out.println("删除");
    }

    public void update() {
        System.out.println("修改");
    }

    public void query() {
        System.out.println("查询");
    }
}
