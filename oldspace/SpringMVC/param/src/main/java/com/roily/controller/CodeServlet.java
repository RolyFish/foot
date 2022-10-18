package com.roily.controller;

import com.roily.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/10
 */
@RequestMapping("/code")
@Controller
public class CodeServlet {

    @RequestMapping(value = "/t1",method = RequestMethod.POST)
    public String t1(User user, Model model) {

        System.out.println(user);

        model.addAttribute("user",user);

        return "param";
    }


}
