package com.camemax.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {

    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            // 定义XML核心配置文件路径信息
            String resource = "mybatis-config.xml";
            // 读取XML核心配置文件路径信息
            InputStream inputStream = Resources.getResourceAsStream(resource);
            // 获得实例化SQLSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
}
