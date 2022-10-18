package com.roily.ioc.service.impl;

import com.roily.ioc.dao.UserDao;
import com.roily.ioc.dao.impl.UserDaoImpl1;
import com.roily.ioc.dao.impl.UserDaoImpl2;
import com.roily.ioc.service.UserService;
import com.roily.ioc.service.UserService2;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2021/12/12
 */
public class UserServiceImpl2 implements UserService2 {

    /**
     * 这里只给一个声明 它的实现是什么并不关心 只需调用其方法就行
     * 但是也得将他实例化
     * 两种方法：
     * 1、set
     *      userdao可以是他的任何实现类
     *      这样就可以少修改点service的代码
     * 2、构造器
     *     有参构造可实现注入
     */
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl2(UserDao userDao) {
        this.userDao = userDao;
    }
    public UserServiceImpl2() {
        this.userDao = new UserDaoImpl1();
    }

    public void getUser() {
        System.out.println("service调用dao");
        userDao.getUser();
    }
}
