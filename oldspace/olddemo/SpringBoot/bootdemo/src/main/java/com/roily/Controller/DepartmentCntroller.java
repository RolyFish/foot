package com.roily.Controller;


import com.roily.Mapper.DepartmentMapper;
import com.roily.POJO.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DepartmentCntroller {

    @Autowired
    private DepartmentMapper departmentMapper;

    @RequestMapping("/dep/list")
    public String list(Model model){
        System.out.println("dep");
        model.addAttribute("deps", departmentMapper.queryDep());
        return "department/list";
    }
    @RequestMapping("/dep/del/{id}")
    public String del(@PathVariable("id")int id, Model model){
        departmentMapper.delDep(id);
        model.addAttribute("deps", departmentMapper.queryDep());
        return "department/list";
    }
    @GetMapping("/dep/edit/{id}")
    public String toedit(@PathVariable("id")int id, Model model){
        Department department = departmentMapper.quertDepMap(id);
        model.addAttribute("dep",department);
        return "department/edit";
    }
    @PostMapping("/dep/edit")
    public String edit(Department dep, Model model){
        departmentMapper.updateDep(dep);
        model.addAttribute("deps", departmentMapper.queryDep());
        return "department/list";
    }
    @RequestMapping("/dep/add")
    public String toadd(){
        return "department/add";
    }
    @PostMapping("/dep/add")
    public String add(Department dep,Model model){
        departmentMapper.addDep(dep);
        model.addAttribute("deps", departmentMapper.queryDep());
        return "department/list";
    }
}
