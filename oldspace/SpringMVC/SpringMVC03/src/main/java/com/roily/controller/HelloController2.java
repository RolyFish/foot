package com.roily.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/c1")
public class HelloController2 {

    @RequestMapping("/t1")
    public String test01(Model model){
        model.addAttribute("msg","c2t1");
        return "hello";
    }

}
