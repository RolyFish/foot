package com.roily.ioc.dao.impl;

import com.roily.ioc.dao.UserDao;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2021/12/12
 */
public class UserDaoImpl1 implements UserDao {

    public void getUser() {
        System.out.println("dao1层得到用户");
    }
}
