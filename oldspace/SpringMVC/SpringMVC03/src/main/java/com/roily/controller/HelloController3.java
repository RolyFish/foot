package com.roily.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/c3")
public class HelloController3 {
    @RequestMapping("/f1")
    public String from(Model model){
        return "from";
    }

    @RequestMapping("/t1")
    public String test01(int a,int b, Model model){
        model.addAttribute("msg","结果为"+(a+b));
        return "hello";
    }
    @RequestMapping("/t2/{a}/{b}")
    public String test02(@PathVariable int a,@PathVariable int b, Model model){
        model.addAttribute("msg","结果为"+(a+b));
        return "hello";
    }
    @RequestMapping(value = "/t3/{a}/{b}", method = RequestMethod.GET )
    public String test03(@PathVariable int a,@PathVariable int b, Model model){
        model.addAttribute("msg","结果为"+(a+b)+"/tt3");
        return "hello";
    }

    @RequestMapping(value = "/t3/{a}/{b}", method = RequestMethod.POST )
    public String test04(@PathVariable int a,@PathVariable int b, Model model){
        model.addAttribute("msg","结果为"+(a+b)+"post");
        return "hello";
    }

}
