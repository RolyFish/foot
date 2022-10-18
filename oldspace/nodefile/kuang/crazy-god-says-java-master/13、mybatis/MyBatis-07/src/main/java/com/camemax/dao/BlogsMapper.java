package com.camemax.dao;

import com.camemax.pojo.Blogs;

import java.util.List;
import java.util.Map;

public interface BlogsMapper {
    Blogs getBlogInfoByAuthor(String author);
}
