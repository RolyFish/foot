package com.roily.controller;

import com.roily.pojo.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class jsonController {

    @RequestMapping("/t1")
    public List<User> t1(){
        User u1 = new User(1, "大", "123");
        User u2 = new User(2, "大", "123");
        User u3 = new User(3, "大", "123");
        List<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        return users;
    }

    @RequestMapping("/t2")
    public String t2(String username){
        String msg = "";
        if(!StringUtils.isEmpty(username)){
            if("admin".equals(username)){
                msg = "可以使用";

            }else {
                msg = "不可以使用";
            }
        }
        return msg;
    }

    @RequestMapping("/tt1")
    public List<User> tt1(){
        List<User> ul = new ArrayList<User>();
        ul.add(new User(1,"于延闯","123"));
        ul.add(new User(2,"于延闯","123"));
        ul.add(new User(3,"于延闯","123"));
        return ul;
    }
    @RequestMapping("/tt2")
    public List<String> tt2(String in1){
        System.out.println(in1);
        ArrayList<String> strings = new ArrayList<String>();
        if("a".equals(in1)){
            strings.add("a1");
            strings.add("a2");
            strings.add("a3");
            strings.add("a4");
            strings.add("a5");
        }
        if("b".equals(in1)){
            strings.add("b1");
            strings.add("b2");
            strings.add("b3");
            strings.add("b4");
            strings.add("b5");
        }
        return strings;
    }
    @RequestMapping("/tt3")
    public String tt3(String username){
        System.err.println(username);
        String msg = "";
        if(username!=null){
            if("admin".equals(username)){
                msg="ok";
            }else {
                msg="error";
            }
        }
        return msg;
    }

}
