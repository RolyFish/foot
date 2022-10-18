package com.roily.ioc.service.impl;

import com.roily.ioc.dao.UserDao;
import com.roily.ioc.dao.impl.UserDaoImpl1;
import com.roily.ioc.dao.impl.UserDaoImpl2;
import com.roily.ioc.service.UserService;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2021/12/12
 */
public class UserServiceImpl implements UserService {
    /**
     * 如果我们想要使用UserDao中的方法
     * 1、声明它
     * 2、想要用就得定义它  new
     *
     * 如果说UserDao有另一个实现类 想用又得去new
     *
     * 改变了UserService  接口 实现 参数
     * 弊端很多
     */
    private UserDao userDao1 = new UserDaoImpl1();

    private UserDao userDao2= new UserDaoImpl2();

    public void getUser1() {
        System.out.println("service 层  调用dao层");
        userDao1.getUser();
    }
    public void getUser2() {
        System.out.println("service 层  调用dao层");
        userDao2.getUser();
    }


}
