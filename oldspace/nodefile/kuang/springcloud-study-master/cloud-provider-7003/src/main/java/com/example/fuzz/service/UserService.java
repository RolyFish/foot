package com.example.fuzz.service;

import com.example.fuzz.dao.UserDao;
import com.example.fuzz.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 13:36
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> selectList() {
        return userDao.selectList();
    }

}
