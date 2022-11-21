package com.roily.Controller;

import com.roily.Mapper.DepartmentMapper;
import com.roily.Mapper.EmployeeMapper;
import com.roily.POJO.Department;
import com.roily.POJO.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class EmplyeeController {

    @Autowired
    @Qualifier("employeeMapper")
    private EmployeeMapper employeeMapper;

    @Autowired
    @Qualifier("departmentMapper")
    private DepartmentMapper departmentMapper;

    @RequestMapping("/user/userlist")
    public String queryUser(Model model) {
        System.out.println("eeee");
        Collection<Employee> employees = employeeMapper.queryEmploy();
        model.addAttribute("employees", employees);
        return "emp/list";
    }

    @GetMapping("/emp")
    public String toAdd(Model model) {
        model.addAttribute("department", departmentMapper.queryDep());
        return "emp/add";
    }

    @PostMapping("/emp")
    public String add(Employee employee) {
        employeeMapper.addEmploy(employee);
        System.out.println(employeeMapper.queryEmploy());
        return "redirect:/main";
    }

    @RequestMapping("/emp/{id}")
    public String del(@PathVariable("id") String id, Model model) {
        System.out.println(id);
        employeeMapper.delEmploy(Integer.parseInt(id));
        Collection<Employee> employees = employeeMapper.queryEmploy();
        model.addAttribute("employees", employees);
        return "emp/list";
    }

    @RequestMapping("/emp/edit/{id}")
    public String toEdit(@PathVariable("id") int id, Model model) {
        Employee employee = employeeMapper.queryEmployMap(id);
        model.addAttribute("employee", employee);
        Collection<Department> departments = departmentMapper.queryDep();
        model.addAttribute("department", departments);
        return "emp/edit";
    }

    @PostMapping("/emp/edit")
    public String edit(Employee employee, Model model) {
        System.out.println(employee);
        employeeMapper.updateEmploy(employee);
        Collection<Employee> employees = employeeMapper.queryEmploy();
        model.addAttribute("employees", employees);
        return "emp/list";
    }
}
