package com.lin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class EncodingController {

    /**
     * 1、过滤器解决乱码
     * 2、HttpServletRequest request
     *   request.setCharacterEncoding("UTF-8"); //乱码
     */

    @Controller
    public class Encoding {
        @RequestMapping("/e/t")
        public String test(Model model,String name){
            model.addAttribute("msg",name); //获取表单提交的值
            return "test"; //跳转到test页面显示输入的值
        }
    }

}
