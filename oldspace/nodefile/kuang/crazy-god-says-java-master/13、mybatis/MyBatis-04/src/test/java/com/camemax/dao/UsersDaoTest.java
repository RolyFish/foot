package com.camemax.dao;

import com.camemax.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class UsersDaoTest {
    @Test
    public void getUserInfoById(){

        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao selectMapper = sqlSession.getMapper(UsersDao.class);

        System.out.println(selectMapper.getUserInfoById(1));
        sqlSession.close();
    }

    @Test
    public void updateUserInfoById(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao updateMapper = sqlSession.getMapper(UsersDao.class);

        int i = updateMapper.updateUserInfoById(1, "Camelot", "Fate/ Grand Order", "Camelot@outlook.com", 1);
        if ( i > 0 ){
            System.out.println("update succeed and commit!!!");
        }
        sqlSession.close();
    }

    @Test
    public void deleteUserInfoById(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao deleteMapper = sqlSession.getMapper(UsersDao.class);

        int i = deleteMapper.deleteUserInfoById(6);
        if (i > 0){
            System.out.println("delete succeed and commit!!!");
        }
    }

    @Test
    public void insertUserInfo(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        UsersDao insertMapper = sqlSession.getMapper(UsersDao.class);

        int i = insertMapper.insertUserInfo(6, "Daiz", "daiz", "Daiz@outlook.com", 1);
        if ( i > 0 ){
            System.out.println("insert succeed and commit!!!");
        }
        sqlSession.close();
    }
}
