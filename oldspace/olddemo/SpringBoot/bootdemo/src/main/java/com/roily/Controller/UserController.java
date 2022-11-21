package com.roily.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @RequestMapping("/user/login")
    public String login(HttpSession session, @RequestParam("userName") String userName, @RequestParam("passWord") String passWord, Model model) {

        if (StringUtils.hasText(userName) && "123456".equals(passWord)) {
            System.out.println("登陆成功");
            session.setAttribute("userInfo",userName);
            return "redirect:/main.html";
        }else {
            model.addAttribute("msg","登陆失败");
            return "index";
        }
    }
    @RequestMapping("/user/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");
        return "index";
    }
}
