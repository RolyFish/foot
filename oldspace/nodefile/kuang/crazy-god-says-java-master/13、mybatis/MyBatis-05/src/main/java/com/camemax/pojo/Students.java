package com.camemax.pojo;

import org.apache.ibatis.type.Alias;

@Alias("students")
public class Students {
    private int sid;
    private String sname;
    private int tid;

    public Students(){};

    public Students(int sid, String sname, int tid) {
        this.sid = sid;
        this.sname = sname;
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "Students{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", tid=" + tid +
                '}';
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
