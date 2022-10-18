package com.roily.controller;

import com.roily.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @RequestMapping("/u1/t1")
    public String test01(String name,Model model){
        System.out.println(name);
        model.addAttribute("msg",name);
        return "hello";
    }
    @RequestMapping("/u1/t2")
    public String test02(@RequestParam("username") String name, Model model){
        System.out.println(name);
        model.addAttribute("msg",name);
        return "hello";
    }
    @RequestMapping("/u1/t3")
    public String test03(User user,Model model){
        System.out.println(user);
        model.addAttribute("msg",user.toString());
        return "hello";
    }
    @RequestMapping("/u1/t4")
    public String test04(){
        return "from";
    }

    @RequestMapping("/u1/t5")
    public String test05(@RequestParam("name") String name,Model model){
        System.out.println(name);
        model.addAttribute("msg",name);
        return "hello";
    }
}
