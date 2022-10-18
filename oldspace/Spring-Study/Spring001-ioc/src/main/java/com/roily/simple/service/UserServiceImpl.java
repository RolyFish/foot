package com.roily.simple.service;

import com.roily.simple.dao.UserDao;
import com.roily.simple.dao.UserDaoImpl;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/9
 */
public class UserServiceImpl implements UserService{
    UserDao userDao = new UserDaoImpl();

    public void getuser() {
        System.out.println("调用UserService");
        userDao.getuser();
    }
}
