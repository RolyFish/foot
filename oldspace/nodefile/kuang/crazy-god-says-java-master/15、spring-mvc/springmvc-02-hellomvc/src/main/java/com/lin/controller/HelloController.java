package com.lin.controller;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
       //ModelAndView模型和视图
        ModelAndView mv = new ModelAndView();

        //封装对象，放在ModeAndView
        mv.addObject("msg","HelloSpringMVC");
        //封装要跳转的视图，放在ModelAndView中
        mv.setViewName("hello");
        return mv;
    }
}
