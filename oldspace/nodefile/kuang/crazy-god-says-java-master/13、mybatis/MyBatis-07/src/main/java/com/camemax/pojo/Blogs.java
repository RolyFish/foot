package com.camemax.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Alias("blogs")
public class Blogs implements Serializable {
    private String id;
    private String title;
    private String author;
    private Date createTime;
    private int views;

    public Blogs(){};

    public Blogs(String blogId, String blogTitle, String blogAuthor, Date createTime, int blogViews) {
        this.id = blogId;
        this.title = blogTitle;
        this.author = blogAuthor;
        this.createTime = createTime;
        this.views = blogViews;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId='" + id + '\'' +
                ", blogTitle='" + title + '\'' +
                ", blogAuthor='" + author + '\'' +
                ", createTime=" + createTime +
                ", blogViews=" + views +
                '}';
    }

    public String getBlogId() {
        return id;
    }

    public void setBlogId(String blogId) {
        this.id = blogId;
    }

    public String getBlogTitle() {
        return title;
    }

    public void setBlogTitle(String blogTitle) {
        this.title = blogTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
