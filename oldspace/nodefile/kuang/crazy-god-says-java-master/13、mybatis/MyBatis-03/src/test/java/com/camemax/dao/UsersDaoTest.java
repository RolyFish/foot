package com.camemax.dao;

import com.camemax.pojo.Users;
import com.camemax.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class UsersDaoTest {

    // 测试log4j的输出功能
    static Logger logger = Logger.getLogger(UsersDaoTest.class);

    @Test
    public void getUsersInfo(){

        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);
        List<Users> users = mapper.getUsersInfo();

        for (Users user : users) {
            System.out.println(user);
        }

        sqlSession.close();
    }
    @Test
    public void getUserInfoById(){

        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);
        Users user = mapper.getUserInfoById(2);

        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void log4jTest(){
        logger.info("info: 日志输出等级【Info】");
        logger.debug("debug: 日志输出等级【DEBUG】");
        logger.error("error: 日志输出等级【ERROR】");
    }

    @Test
    public void getUsersInfoByLimit(){

        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        // 指定HashMap传值给映射器Mapper
        // startIndex => 2
        // returnSize => 2
        HashMap<String,Integer> limitMap = new HashMap<String, Integer>();
        limitMap.put("startIndex",2);
        limitMap.put("returnSize",2);

        List<Users> users = mapper.getUsersInfoByLimit(limitMap);
        for (Users user : users) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void getUsersInfoByAnnotation(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        List<Users> users = mapper.getUsersInfoByAnnotation();
        for (Users user : users) {
            System.out.println(user);
        }

        sqlSession.close();
    }
}
