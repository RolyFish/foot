package com.roily.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class userController {

    @RequestMapping("/tologin")
    public String tologin(){
        System.out.println("tologin");
        return "tologin";
    }
    @RequestMapping("/login")
    public String login(String username, String password, Model model, HttpSession session){
        session.setAttribute("loginfo",username);
        System.out.println("登陆操作");
        model.addAttribute("username",username);
        model.addAttribute("password",password);
        return "main";
    }
    @RequestMapping("/main")
    public String tomain(){
        return "main";
    }
    @RequestMapping("/goout")
    public String goout(HttpSession session){
        session.removeAttribute("loginfo");
        return "tologin";
    }


}
