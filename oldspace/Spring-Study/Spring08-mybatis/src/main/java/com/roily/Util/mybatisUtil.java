package com.roily.Util;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class mybatisUtil {
    private  static SqlSessionFactory sqlSessionFactory;
    static {

        String resource = "mybatis-config.xml";
        try {
            InputStream in = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  SqlSession getSqlSession(){
        return sqlSessionFactory.openSession(true);
    }
}
