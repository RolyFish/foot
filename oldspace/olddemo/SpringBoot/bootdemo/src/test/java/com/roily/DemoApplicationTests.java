package com.roily;

import com.roily.Mapper.DepartmentMapper;
import com.roily.Mapper.EmployeeMapper;
import com.roily.POJO.Department;
import com.roily.POJO.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired

    private DataSource dataSource;

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }
    @Test
    void t2() throws SQLException {
        List<Department> departments = departmentMapper.queryDep();
        for (Department department : departments) {
            System.out.println(department);
        }
    }
    @Test
    void t3() throws SQLException {
        Department department1 = new Department();
        department1.setId(1);
        department1.setDepartmentName("打架部");
        int i = departmentMapper.addDep(department1);
    }
    @Test
    void t4() throws SQLException {
        Department department1 = new Department();
        department1.setId(1);
        department1.setDepartmentName("教学部");
        int i = departmentMapper.updateDep(department1);
        System.out.println(i);
    }
    @Test
    void t5() throws SQLException {
        int i = departmentMapper.delDep(5);
        System.out.println(i);
    }
    @Test
    void t6() throws SQLException {
        List<Employee> employees = employeeMapper.queryEmploy();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }
    @Test
    void t7() throws SQLException {
        Employee employee = employeeMapper.queryEmployMap(10);
        System.out.println(employee);
    }
    @Test
    void t9() throws SQLException {
        employeeMapper.delEmploy(11);
    }
    @Test
    void t8() throws SQLException {
        Employee employee = employeeMapper.queryEmployMap(1);
        System.out.println(employee);

        for(int i=0;i<10;i++){
            employee.setDepartmentid(i%5);
            employee.setEmail(i+"@qq.com");
            employee.setGender(i%2);
            employee.setLastName("于延闯".toString());
            employeeMapper.addEmploy(employee);
        }
    }
    @Test
    void t10() throws SQLException {
        Employee employee = employeeMapper.queryEmployMap(1);
        System.out.println(employee);
            employee.setEmail("xiugai"+"@qq.com");
            employee.setGender(0);
            employee.setLastName("于延闯123".toString());
            employeeMapper.updateEmploy(employee);

    }



}
