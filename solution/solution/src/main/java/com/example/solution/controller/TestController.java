package com.example.solution.controller;

import com.example.solution.common.config.ComponentBean;
import com.example.solution.service.IService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TestController {

    private IService iService;

    @PostMapping("/test")
    public void test() {
        iService.service();
    }
}
