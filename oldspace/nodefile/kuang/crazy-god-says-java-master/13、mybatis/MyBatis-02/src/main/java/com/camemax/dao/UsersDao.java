package com.camemax.dao;

import com.camemax.pojo.Users;

import java.util.List;

public interface UsersDao {

    List<Users> getUsersInfo();

    int updateUseInfoById(Users user);

    int deleteUserById(int id);

    int insertManyUseList(List<Users> usersList);
}
