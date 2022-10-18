package com.lin.demo02;

public class UserServiceProxy implements UserService {
//代理模式
    private UserServiceImpl userService;

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    public void add() {
        log("add");
        userService.add();
    }

    public void delete() {
        log("delete");
        userService.delete();
    }

    public void update() {
        log("update");
        userService.update();
    }

    public void query() {
        log("query");
        userService.query();
    }
    //日志方法
    public void log(String msg){
        System.out.println("[DEBUG]使用了"+msg+"方法");
    }
}
