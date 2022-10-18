package com.lin.service;

import com.lin.dao.UserDao;
import com.lin.dao.UserDaoImpl;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    //1--private UserDao userDao = new UserDaoImpl();

    //2--利用set进行动态实现值的注入！
    public void setUserDao(UserDao userDao){
        this.userDao=userDao;
    }

    public void getUser() {
        userDao.getUser();
    }

}
