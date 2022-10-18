package com.lin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller//走视图页面
@RequestMapping("/user")
public class LoginController {

    @RequestMapping("/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/goLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(HttpSession session, String username, String password, Model model){
        System.out.println("login==>"+username);
        //把用户的信息存在session中；
        session.setAttribute("userLoginInfo",username);
        model.addAttribute("username",username);
        return "main";
    }

    @RequestMapping("/goOut")
    public String goOut(HttpSession session){
        //方式1，销毁
        //session.invalidate();
        //方式2.把用户的信息从session中删除；推荐
        session.removeAttribute("userLoginInfo");

        return "login";
    }
}
