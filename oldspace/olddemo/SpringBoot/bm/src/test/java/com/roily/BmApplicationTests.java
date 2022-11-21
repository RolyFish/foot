package com.roily;

import com.roily.Mapper.UserMapper;
import com.roily.POJO.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest
class BmApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {

        System.out.println(dataSource.getClass());
    }

    @Test
    void contextLoads2() {
        List<User> users = userMapper.queryUserList();
        System.out.println(users);
        System.out.println(dataSource.getClass());
    }

}
