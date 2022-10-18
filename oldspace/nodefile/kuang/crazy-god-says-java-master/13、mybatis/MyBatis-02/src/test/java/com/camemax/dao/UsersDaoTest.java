package com.camemax.dao;

import com.camemax.pojo.Users;
import com.camemax.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UsersDaoTest {

    @Test
    public void getUsersInfo(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        List<Users> usersInfo = mapper.getUsersInfo();

        for (Users users : usersInfo) {
            System.out.println(users);
        }

        sqlSession.close();
    }
}
