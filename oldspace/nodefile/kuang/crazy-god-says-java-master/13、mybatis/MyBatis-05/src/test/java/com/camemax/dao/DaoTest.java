package com.camemax.dao;

import com.camemax.pojo.Students;
import com.camemax.pojo.Teachers;
import com.camemax.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class DaoTest {

    @Test
    public void getStudentsInfo(){

        MyBatisUtils myBatisUtils = new MyBatisUtils();
        SqlSession sqlSession = myBatisUtils.getSqlSession();
        StudentsMapper mapper = sqlSession.getMapper(StudentsMapper.class);

        for (Students student : mapper.getStudentsInfo()) {
            System.out.println(student);
        }

        sqlSession.close();
    }

    @Test
    public void getTeacherByIdHasStudents(){
        SqlSession sqlSession = new MyBatisUtils().getSqlSession();
        TeachersMapper mapper = sqlSession.getMapper(TeachersMapper.class);
        System.out.println(mapper.getTeacherByIdHasStudents(1));

        sqlSession.close();
    }
}
