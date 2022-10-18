package com.camemax.dao;

import com.camemax.pojo.Users;
import com.camemax.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UsersDaoTest {

    // 单元测试： 获取所有用户信息
    @Test
    public void getUsersInfo(){

        // 调用MyBatisUtils.getSqlSession()方法，获取SqlSession对象
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        // 调用获取到的SQLSession对象中的getMapper对象
        // 反射Dao接口，动态代理Dao接口中的方法，并将这些方法存在对象【mapper】中
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        // 调用mapper中对应方法，并设置对应的对象来接收其返回结果
        // 以下为测试方法getUsersInfo() => 获取所有Users表中信息，并用对应类接收
        List<Users> usersInfo = mapper.getUsersInfo();

        // for循环遍历输出List集合
        for (Users users : usersInfo) {
            System.out.println(users);
        }
        // 关闭sqlSession
        sqlSession.close();

    }

    // 单元测试： 获取指定用户信息
    @Test
    public void getUserInfoById(){
        // 调用MyBatisUtils.getSqlSession()方法，获取SqlSession对象
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        // 调用获取到的SQLSession对象中的getMapper对象
        // 反射Dao接口，动态代理Dao接口中的方法，并将这些方法存在对象【mapper】中
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        Users user = mapper.getUserInfoById(2);
        System.out.println(user);

        // 关闭sqlSession
        sqlSession.close();
    }

    // 单元测试： 单行插入指定用户信息
    @Test
    public void insertUsers(){
        // 调用MyBatisUtils.getSqlSession()方法，获取SqlSession对象
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        // 调用获取到的SQLSession对象中的getMapper对象
        // 反射Dao接口，动态代理Dao接口中的方法，并将这些方法存在对象【mapper】中
        UsersDao mapper = sqlSession.getMapper(UsersDao.class);
/*
        int i1 = mapper.insertUser(new Users(2, "Aurthur", "aurthur", "Aurthur@outlook.com", 0));
        int i2 = mapper.insertUser(new Users(3, "Nero", "nero", "Nero@outlook.com", 0));
        int i3 = mapper.insertUser(new Users(4, "Gawain", "gawain", "Gawain@outlook.com", 1));
        int i4 = mapper.insertUser(new Users(5, "Lancelot", "lancelot", "Lancelot@outlook.com", 1));
*/
        int i = mapper.insertUser(
                new Users(2, "Aurthur", "aurthur", "Aurthur@outlook.com", 0)
                /*
                ,new Users(3, "Nero", "nero", "Nero@outlook.com", 0)
                ,new Users(4, "Gawain", "gawain", "Gawain@outlook.com", 1)
                ,new Users(5, "Lancelot", "lancelot", "Lancelot@outlook.com", 1)
                */
        );

        //提交事务
        sqlSession.commit();
        if ( i > 0 ){
            System.out.println("Insert Successful!");
        }

        // 关闭sqlSession
        sqlSession.close();
    }

    @Test
    public void deleteUserInfoById(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        String willDeleteUsername = mapper.getUserInfoById(2).getUsername();
        int i = mapper.deleteUserById(2);

        if (i > 0){
            System.out.println(willDeleteUsername + " has been deleted!");
        }

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUseInfoById(){

        SqlSession session = MyBatisUtils.getSqlSession();
        UsersDao mapper = session.getMapper(UsersDao.class);
        int i = mapper.updateUseInfoById(new Users(1, "Camelot", "Fate/Grand Order", "Camelot@outlook.com", 1));
        if ( i > 0 ){
            System.out.println(mapper.getUserInfoById(1).getUsername() + " has been updated!");
        }
        session.commit();
        session.close();
    }

    @Test
    public void insertManyUseList(){

        List<Users> users = new ArrayList<Users>();
        users.add(new Users(2, "Aurthur", "aurthur", "Aurthur@outlook.com", 0));
        users.add(new Users(3, "Nero", "nero", "Nero@outlook.com", 0));
        users.add(new Users(4, "Gawain", "gawain", "Gawain@outlook.com", 1));
        users.add(new Users(5, "Lancelot", "lancelot", "Lancelot@outlook.com", 1));

        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        int i = mapper.insertManyUseList(users);

        if ( i > 0 ){

            System.out.println("Insert Many Finished and Successful!");
        }
        sqlSession.commit();

        sqlSession.close();
    }

    @Test
    public void getUsersInfoByPhantomSelect(){

        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UsersDao mapper = sqlSession.getMapper(UsersDao.class);

        List<Users> users = mapper.getUsersInfoByPhantomSelect("%e%");

        for (Users user : users) {
            System.out.println(user);
        }

        sqlSession.close();
    }
}