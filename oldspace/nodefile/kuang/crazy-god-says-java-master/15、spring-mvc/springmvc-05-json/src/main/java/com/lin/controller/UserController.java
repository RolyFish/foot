package com.lin.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lin.pojo.User;
import com.lin.util.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
//@RestController=Controller+ResponseBody
public class UserController {

    @RequestMapping("/j1")
    @ResponseBody//它就不会走视图解析器，会直接返回一个字符串
    public String json1() throws JsonProcessingException {

        //jackson,ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        //创建一个对象
        User user = new User("林",18,"男");

        String str = mapper.writeValueAsString(user);

        return str;
    }

    @RequestMapping("/j2")
    @ResponseBody//它就不会走视图解析器，会直接返回一个字符串
    public String json2() throws JsonProcessingException {

        //jackson,ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = new ArrayList<User>();
        //创建一个对象
        User user1 = new User("林1",18,"男");
        User user2 = new User("林2",18,"男");
        User user3 = new User("林3",18,"男");
        User user4 = new User("林4",18,"男");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        String str = mapper.writeValueAsString(userList);

        return str;
    }

//    @RequestMapping("/j3")
//    @ResponseBody//它就不会走视图解析器，会直接返回一个字符串
//    public String json3() throws JsonProcessingException {
//        //jackson,ObjectMapper
//        ObjectMapper mapper = new ObjectMapper();
//
//        //不使用时间戳的方式
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//
//        //自定义日期格式对象
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        mapper.setDateFormat(sdf);
//
//        Date date = new Date();
//        //ObjectMapper,时间解析后的默认格式为：Timestamp： 时间戳
//
//        return mapper.writeValueAsString(date);
//    }
    @RequestMapping("/j3")
    @ResponseBody//它就不会走视图解析器，会直接返回一个字符串
    public String json3() throws JsonProcessingException {

        Date date = new Date();
        return JsonUtil.getJson(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * alibaba
     * fastJson
     */
    @RequestMapping("/j4")
    @ResponseBody//它就不会走视图解析器，会直接返回一个字符串
    public String json4() throws JsonProcessingException {

        List<User> userList = new ArrayList<User>();
        //创建一个对象
        User user1 = new User("林1",18,"男");
        User user2 = new User("林2",18,"男");
        User user3 = new User("林3",18,"男");
        User user4 = new User("林4",18,"男");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        String s = JSON.toJSONString(userList);
        return s;
    }
}
