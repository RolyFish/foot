package com.roily.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roily.Util.JsonUtil;
import com.roily.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class jsonController {

    @ResponseBody
    @RequestMapping("/j1")
    public String json1() {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("于延闯", 23, "男");
        String s = null;
        try {
            s = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }
    @ResponseBody
    @RequestMapping("/j2")
    public String json2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<User> users = new ArrayList<User>();
        User user1 = new User("于延闯", 23, "男");
        User user2 = new User("于延闯", 23, "男");
        User user3 = new User("于延闯", 23, "男");
        User user4 = new User("于延闯", 23, "男");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        String s = objectMapper.writeValueAsString(users);
        return s;
    }
    @ResponseBody
    @RequestMapping("/j3")
    public String json3() throws JsonProcessingException {
        Date date = new Date();
        return new ObjectMapper().writeValueAsString(date);
    }
    @ResponseBody
    @RequestMapping("/j4")
    public String json4() throws JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date date = new Date();
        String format = simpleDateFormat.format(date);
        return new ObjectMapper().writeValueAsString(format);
    }
    @ResponseBody
    @RequestMapping("/j5")
    public String json5() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("m1","string");
        map.put("m2",2);
        map.put("m3",new User("于延闯",23,"男"));
        map.put("m4",new Date());
        return new ObjectMapper().writeValueAsString(map);
    }
    @ResponseBody
    @RequestMapping("/j6")
    public String json6()  {
        return  JsonUtil.getJson(new Date());
    }
    @ResponseBody
    @RequestMapping("/j7")
    public String json7()  {
        return  JsonUtil.getJson(new User("鸳鸯奶茶",12,"爸爸"));
    }


    @ResponseBody
    @RequestMapping("/f1")
    public String fj1() {
        User user = new User("于延闯", 23, "男");
        String s = null;
        User parse = JSON.parseObject("{'name':'鸳鸯奶茶','age':'12','sex':'男'}",User.class);

        System.out.println(parse);

        s = JSON.toJSONString(user);
        return s;
    }
    @ResponseBody
    @RequestMapping("/f2")
    public String fj2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<User> users = new ArrayList<User>();
        User user1 = new User("于延闯", 23, "男");
        User user2 = new User("于延闯", 23, "男");
        User user3 = new User("于延闯", 23, "男");
        User user4 = new User("于延闯", 23, "男");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        String s = objectMapper.writeValueAsString(users);
        return s;
    }
}
