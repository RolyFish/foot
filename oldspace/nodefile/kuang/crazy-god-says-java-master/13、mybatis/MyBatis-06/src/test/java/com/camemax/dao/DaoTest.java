package com.camemax.dao;

import com.camemax.pojo.Blogs;
import com.camemax.utils.MyBatisUtils;
import com.camemax.utils.UUIDUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.*;

public class DaoTest {

    public SqlSession getSqlSession(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        return sqlSession;
    }

    @Test
    public void addBlog(){

        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

        Blogs blogs = new Blogs();
        blogs.setBlogId(UUIDUtils.createUUID());
        blogs.setBlogTitle("MyBatis");
        blogs.setAuthor("Camemax");
        blogs.setCreateTime(new Date());
        blogs.setViews(9999);
        int i1 = mapper.addBlog(blogs);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blogs.setBlogId(UUIDUtils.createUUID());
        blogs.setBlogTitle("Spring");
        blogs.setAuthor("Aurthur");
        blogs.setCreateTime(new Date());
        int i2 = mapper.addBlog(blogs);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blogs.setBlogId(UUIDUtils.createUUID());
        blogs.setAuthor("Artria");
        blogs.setBlogTitle("Spring Framework");
        blogs.setCreateTime(new Date());
        int i3 = mapper.addBlog(blogs);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blogs.setBlogId(UUIDUtils.createUUID());
        blogs.setAuthor("Camelot");
        blogs.setBlogTitle("Spring Boot");
        blogs.setCreateTime(new Date());
        int i4 = mapper.addBlog(blogs);

        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        blogs.setBlogId(UUIDUtils.createUUID());
        blogs.setBlogTitle("Waiting Update Title");
        blogs.setAuthor("Waiting Update Author");
        blogs.setCreateTime(new Date());
        blogs.setViews(0);
        int i5 = mapper.addBlog(blogs);

        if ( i1 > 0 && i2 > 0 && i3 > 0 && i4 > 0 && i5 > 0 ){
            System.out.println("add succeed");
        }

        sqlSession.close();
    }

    @Test
    public void dynamicSqlTest(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

        Map<String,String> map = new HashMap<String, String>();
        map.put("title","MyBatis");
        map.put("author","Camemax");

        List<Blogs> blogs = mapper.getBlogs(map);
        for (Blogs blog : blogs) {
            System.out.println(blog);
        }

        myBatisUtils.getSqlSession().close();
    }

    @Test
    public void dynamicSqlChoose(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

        HashMap<String,String> map = new HashMap<String, String>();
        //map.put("title","MyBatis");
        map.put("author","Camelot");
        System.out.println(mapper.queryBlogsByChoose(map));

        sqlSession.close();
    }

    @Test
    public void dynamicSqlUpdateBySet(){
        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("title","updatedTitle");
        map.put("author","updatedAuthor");
        map.put("createTime", String.valueOf(new Date()));
        map.put("views","1");
        map.put("id","5bde3e48b521443bb40524988456a668");

        int i = mapper.updateBlogInfoBySet(map);
        if (i > 0 ){
            System.out.println("Update Succeed!");
        }
        sqlSession.close();
    }

    @Test
    public void dynamicSqlUpdateByTrim(){

        SqlSession sqlSession = getSqlSession();
        BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

        Map<String,String> map = new HashMap<String, String>();
        map.put("titleMap","Spring Framework Updated");
        map.put("authorMap","Altria");
        map.put("idMap","5aa45402bc764755b3ae406be6b27d33");

        int i = mapper.updateBlogInfoByTrim(map);
        if ( i > 0 ){
            System.out.println("Update Succeed!");
        }

    }

    @Test
    public void dynamicSqlSelectByTrim(){
        SqlSession sqlSession = getSqlSession();
        BlogsMapper mapper = sqlSession.getMapper(BlogsMapper.class);

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("titleMap","Spring Framework Updated");
        map.put("authorMap","Altria");

        for (Blogs blog : mapper.queryBlogsByTrim(map)) {
            System.out.println(blog);
        }

        sqlSession.close();
    }
}
