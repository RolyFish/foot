package com.roily.Mapper;

import com.roily.POJO.Department;
import com.sun.tracing.dtrace.ModuleAttributes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentMapper {
    public List<Department> queryDep();
    public Department quertDepMap(@Param("id") int id);
    public int delDep(@Param("id") int id);
    public int updateDep(Department department);
    public int addDep(Department department);
}
