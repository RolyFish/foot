package com.roily.Mapper;

import com.roily.POJO.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmployeeMapper {

    public List<Employee> queryEmploy();
    public Employee queryEmployMap(@Param("id") int id);
    public int addEmploy(Employee employee);
    public int delEmploy(@Param("id") int id);
    public int updateEmploy(Employee employee);

}
