package com.camemax.dao;

import com.camemax.pojo.Blogs;
import com.camemax.utils.MyBatisUtils;
import com.camemax.utils.UUIDUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoTest {

    public SqlSession getSqlSession(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        return sqlSession;
    }

        @Test
        public void firstLevelCacheTest(){

            SqlSession sqlSession = getSqlSession();
            BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

            // 第一次执行执行SqlSession
            Blogs artria1 = mapper.getBlogInfoByAuthor("Altria");
            System.out.println(artria1);

            sqlSession.commit();

            // 第二次执行SqlSession
            Blogs artria2 = mapper.getBlogInfoByAuthor("Altria");
            System.out.println(artria2);

            // 对比两个对象是否相同
            System.out.println(artria1 == artria2);
            sqlSession.close();
        }
}
