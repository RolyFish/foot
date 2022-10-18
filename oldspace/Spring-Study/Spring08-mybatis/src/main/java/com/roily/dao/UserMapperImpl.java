package com.roily.dao;

import com.roily.pojo.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserMapperImpl implements UserMapper {
    @Resource
    private  SqlSessionTemplate sqlSessionTemplate;

//    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
//        this.sqlSessionTemplate = sqlSessionTemplate;
//    }
    @Override
    public List<User> getUserList() {
        UserMapper mapper = sqlSessionTemplate.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        return userList;
    }

    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public int delUser(int id) {
        return 0;
    }
}
