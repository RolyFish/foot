package com.roily;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JdbcApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource);
        Connection connection = dataSource.getConnection();
        String sql = "select * from `users` where id=? ";
        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setObject(1, 5);
        ResultSet resultSet = prep.executeQuery();
        while
        (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
        }
        prep.close();
        connection.close();
    }

    @Test
    void contextLoads2() throws SQLException {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from users ");

        System.out.println(maps.get(0).get("id"));

        System.out.println(jdbcTemplate.getDataSource());
    }

    @Test
    void contextLoads3() throws SQLException {

        System.out.println(dataSource.getConnection());

        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        System.out.println("druidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
        System.out.println("druidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());
    }

    @Test
    void contextLoads4() throws SQLException {

        Object[] objects = new Object[1];
        objects[0] = 5;
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from users where id=?", objects);
        for (Map<String, Object> map : maps) {
            System.out.println(map);
        }
    }


}
