package com.roily.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roily.POJO.Cat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author RoilyFish
 */
@Controller
public class TestController {

    @RequestMapping("/t1")
    @ResponseBody
    public String  t1() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add(1);
        list.add(new String("nihao"));
        String s = objectMapper.writeValueAsString(list);
        return s;
    }

    @RequestMapping("/t11")
    @ResponseBody
    public String  t11(String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add(1);
        list.add(new String("nihao"));
        list.add(str);
        String s = objectMapper.writeValueAsString(list);
        return s;
    }

    @RequestMapping(value = "/t111",method = RequestMethod.POST)
    @ResponseBody
    public String  t111(String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add(1);
        list.add(new String("nihao"));
        list.add(str);
        String s = objectMapper.writeValueAsString(list);
        return s;
    }


    @RequestMapping("/t2")
    public String  t2(Model model) {
        model.addAttribute("msg","ndahao吧");
        Cat cat = new Cat();
        cat.setAge(1);
        cat.setName("猫咪");
        model.addAttribute("cat",cat);
        List<Object> list = new ArrayList<>();
        list.add(new Cat("cat1",1));
        list.add(new Cat("cat2",2));
        list.add(new Cat("cat3",3));
        model.addAttribute("catList",list);

        Map<Object, Object> map = new HashMap<>();

        map.put("name","于延闯");
        map.put("age",1);
        map.put("cat",cat);
        model.addAttribute("map",map);
        return "test";
    }

    @RequestMapping("/t3")
    public String t3(Model model){
        model.addAttribute("msg","yycas萨达撒");
        return "test2";
    }
    @RequestMapping("/t4")
    public String t4(Model model){
        model.addAttribute("msg","dasdsad");
        return "test3";
    }
    @RequestMapping("/hellot")
    public String helloT(Model model){
        model.addAttribute("msg","helloT");
        return "hello";
    }


}
