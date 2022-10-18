package com.roily.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Roly_Fish
 */
@Controller
public class HelloController {

    @RequestMapping("/hello1")
    public String hello(Model model, HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException , ServletException {
        req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req,resp);
        model.addAttribute("msg","annocation springmvc");
        return "hello";
    }

    @RequestMapping("/hello2")
    public String hello2(Model model){
        model.addAttribute("msg","annocation springmvc");
        return "hello";
    }
}
