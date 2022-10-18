package com.camemax.dao;

import com.camemax.pojo.Students;
import com.camemax.pojo.Teachers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeachersMapper {

    List<Students> getTeacherByIdHasStudents(@Param("tid") int tid);
}
