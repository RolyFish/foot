package com.lin.mapper;

import com.lin.pojo.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl extends SqlSessionDaoSupport implements UserMapper {

    public List<User> selectUser() {

        User user = new User(5, "5哥", "123456");


        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);//SqlSessionDapSupport

        mapper.addUser(user);
        mapper.deleteUser(5);

        return mapper.selectUser();
    }

    public int addUser(User user) {
        return getSqlSession().getMapper(UserMapper.class).addUser(user);
    }

    public int deleteUser(int id) {
        return getSqlSession().getMapper(UserMapper.class).deleteUser(id);
    }

    /**private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<User> selectUser() {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        return mapper.selectUser();
    }*/

}
