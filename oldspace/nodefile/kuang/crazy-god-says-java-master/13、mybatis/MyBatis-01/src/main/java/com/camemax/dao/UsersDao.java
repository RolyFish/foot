package com.camemax.dao;

import com.camemax.pojo.Users;

import java.util.List;
import java.util.Map;

public interface UsersDao {

    // 【select】所有用户信息
    List<Users> getUsersInfo();

    // 【select】指定用户信息
    Users getUserInfoById(int id);

    // 【update】指定用户信息
    int updateUseInfoById(Users user);

    // 【insert】指定用户信息
    int insertUser(Users user);

    // 【delete】指定用户信息
    int deleteUserById(int id);

    // 【insert】 批量用户信息
    int insertManyUseList(List<Users> users);

    // 【select】 模糊查询
    List<Users> getUsersInfoByPhantomSelect(String username);
}