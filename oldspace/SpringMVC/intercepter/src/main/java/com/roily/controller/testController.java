package com.roily.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class testController {

    @GetMapping("/t1/{name}")
    public String t1(@PathVariable("name") String username){

        return username;
    }
    @GetMapping("/t1")
    public void t2(HttpServletRequest req , HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }
}
