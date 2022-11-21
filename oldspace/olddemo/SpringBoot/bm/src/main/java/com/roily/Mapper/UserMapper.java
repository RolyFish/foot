package com.roily.Mapper;

import com.roily.POJO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    public List<User> queryUserList();
    public User queryUserMap(@Param("id") int id);
    public int addUser(User user);
    public int updateUser(User user);
    public int delUser(@Param("id") int id);
}
