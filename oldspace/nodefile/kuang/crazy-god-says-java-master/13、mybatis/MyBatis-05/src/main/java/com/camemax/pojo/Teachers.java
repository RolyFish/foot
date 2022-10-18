package com.camemax.pojo;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("teachers")
public class Teachers {
    private int tid;
    private String tname;
    // 新增属性 ： 教师拥有的学生
    private List<Students> teacherHasStudents;

    public List<Students> getTeacherHasStudents() {
        return teacherHasStudents;
    }

    public void setTeacherHasStudents(List<Students> teacherHasStudents) {
        this.teacherHasStudents = teacherHasStudents;
    }

    public Teachers(int tid, String tname, List<Students> teacherHasStudents) {
        this.tid = tid;
        this.tname = tname;
        this.teacherHasStudents = teacherHasStudents;
    }

    public Teachers() {};

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    @Override
    public String toString() {
        return "Teachers{" +
                "tid=" + tid +
                ", tname='" + tname + '\'' +
                ", teacherHasStudents=" + teacherHasStudents +
                '}';
    }
}
