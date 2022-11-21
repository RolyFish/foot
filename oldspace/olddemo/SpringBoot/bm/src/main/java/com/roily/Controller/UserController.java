package com.roily.Controller;

import com.roily.Mapper.UserMapper;
import com.roily.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    public UserMapper userMapper;

    @RequestMapping("/query")
    public List<User> query(){
        List<User> users = userMapper.queryUserList();
        return users;
    }
    @RequestMapping("/test")
    @ResponseBody
    public String t(){
        List<User> users = userMapper.queryUserList();
        return "tes2qewedat";
    }
    @RequestMapping("/query/{id}")
    public User queryMap(@PathVariable("id") int id){
        User user = userMapper.queryUserMap(id);
        return user;
    }
    @RequestMapping("/update/{id}/{name}")
    public List<User> update(@PathVariable("id") int id,@PathVariable("name") String name){
        User user = new User(id, name, "123");
        int i = userMapper.updateUser(user);
        List<User> users = userMapper.queryUserList();
        return users;
    }
    @RequestMapping("/del/{id}")
    public List<User> query(@PathVariable("id")int id){
        userMapper.delUser(id);
        List<User> users = userMapper.queryUserList();
        return users;
    }
    @RequestMapping("/transcation/{id}")
    public List<User> trans(@PathVariable("id")int id){
        try {
            userMapper.delUser(id);
            userMapper.updateUser(new User(id,"123","123"));
        }catch (Exception e){
            System.err.println("error");
        }
        List<User> users = userMapper.queryUserList();
        return users;
    }
}
