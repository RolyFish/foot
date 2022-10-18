package com.camemax.dao;

import com.camemax.pojo.Users;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UsersDao {

    List<Users> getUsersInfo();

    Users getUserInfoById(int id);

    List<Users> getUsersInfoByLimit(Map<String,Integer> map);

    @Select("select * from school.users")
    List<Users> getUsersInfoByAnnotation();
}
