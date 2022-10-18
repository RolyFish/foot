package com.camemax.dao;

import com.camemax.pojo.Blogs;
import java.util.List;
import java.util.Map;

public interface BlogsMapper {

    int addBlog(Blogs blog);

    List<Blogs> getBlogs(Map<String,String> map);

    List<Blogs> queryBlogsByChoose(Map<String,String> map);

    int updateBlogInfoBySet(Map<String,String> map);

    List<Blogs> queryBlogsByTrim(Map<String,String> map);

    int updateBlogInfoByTrim(Map<String,String> map);
}
