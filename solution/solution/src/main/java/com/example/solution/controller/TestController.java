package com.example.solution.controller;

import com.example.solution.common.aspect.annotation.LogAnnotation;
import com.example.solution.common.config.ComponentBean;
import com.example.solution.common.util.SpringUtils;
import com.example.solution.service.IService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@RestController
public class TestController {

    private IService iService;

    @LogAnnotation(value = "test方法")
    @PostMapping("/test")
    public void test() {
        iService.service();
    }

    /**
     * 测试form-data 或者 url 传参  参数放入 HttpServletRequest
     * @return
     */
    @PostMapping("/test2")
    public String test2() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes1 =  (ServletRequestAttributes) attributes;
        HttpServletRequest request = attributes1.getRequest();
        String test = request.getParameter("test");
        System.out.println(test);
        return test;
    }

    @PostMapping("/test3")
    public String test3(@RequestParam String property) {
        String[] activeProfiles = SpringUtils.getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            System.out.println(activeProfile);
        }
        return SpringUtils.getRequiredProperty(property);
    }

}
