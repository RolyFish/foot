package com.roily.Mapper;


import com.roily.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Roly_Fish
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * queryAllUser
     * @return userList
     */
    List<User> queryUsers();
}
