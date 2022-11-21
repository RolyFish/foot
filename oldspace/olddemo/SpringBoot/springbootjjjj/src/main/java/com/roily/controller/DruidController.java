package com.roily.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Roly_Fish
 */
@RestController
public class DruidController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/test1")
    public Collection test1(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select *from users");
        return maps;
    }

    @RequestMapping("/test2")
    public Collection test2(){
        Object[] objects = new Object[1];
        objects[0] = "123";

        ArrayList<Object> list = new ArrayList<>();
        Collection list1 = list;
        list1.add("123");
        Object[] objects1 = list1.toArray();

        return   jdbcTemplate.queryForList("select * from users where pwd =?",objects1);
    }

}
