package com.roily.controller;

import com.alibaba.fastjson.JSON;
import com.roily.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class testController {

    @RequestMapping("/test")
    public String test01(Model model) {
        User user = new User(1, "你好", "12");
        model.addAttribute("user", user);
        model.addAttribute("msg", "test");
        return "test";
    }

    @RequestMapping("/test2")
    public void test02(String name, HttpServletResponse resp) throws IOException {
        System.out.println(name);
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write("阿贾克斯");
    }

    @RequestMapping(value = "/ajax1",method = RequestMethod.POST)
    @ResponseBody
    public String ajax1() {
        return "请求成功";
    }

    @RequestMapping(value = "/ajax2",method = RequestMethod.POST)
    @ResponseBody
    public String ajax2(String name) {
        String msg = "";
        if ("admin".equals(name)){
            msg = "成功";
        }else {
            msg = "失败";
        }
        return msg;
    }


}
