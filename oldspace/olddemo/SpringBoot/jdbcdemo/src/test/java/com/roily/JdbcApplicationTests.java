package com.roily;

import com.roily.Mapper.UserMapper;
import com.roily.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class JdbcApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() throws SQLException {
        //数据源
        System.out.println(dataSource.getClass());
        //连接
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

    }
    @Test
    void contextLoads2() throws SQLException {
        List<User> users = userMapper.queryUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }
}
