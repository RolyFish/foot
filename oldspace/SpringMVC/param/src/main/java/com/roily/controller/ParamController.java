package com.roily.controller;

import com.roily.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/10
 */
@RequestMapping("/param")
@Controller
public class ParamController {
    
    @RequestMapping(value = "/t1",method = RequestMethod.GET)
    public String t1(String name, Model model){

        System.out.println(name);

        model.addAttribute("name",name);

        return "param";
    }

    @RequestMapping(value = "/t2",method = RequestMethod.GET)
    public String t2(@RequestParam("realName") String name, Model model){

        System.out.println(name);

        model.addAttribute("name",name);

        return "param";
    }

    @RequestMapping(value = "/t3",method = RequestMethod.GET)
    public String t3(User user, Model model){

        System.out.println(user.toString());

        model.addAttribute("user",user);

        return "param";
    }

    @RequestMapping(value = "/t4",method = RequestMethod.GET)
    public String t4(User user, ModelMap model){

        System.out.println(user.toString());

        model.addAttribute("user",user);

        return "param";
    }
    
    
    
}
