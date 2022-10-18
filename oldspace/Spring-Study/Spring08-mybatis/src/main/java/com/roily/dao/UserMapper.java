package com.roily.dao;

import com.roily.pojo.User;

import java.util.List;

public interface UserMapper {
    public List<User> getUserList();
    public int addUser(User user);
    public int delUser(int id);
}
