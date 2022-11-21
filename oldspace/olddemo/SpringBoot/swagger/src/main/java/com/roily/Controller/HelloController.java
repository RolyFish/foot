package com.roily.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author  roilyfish
 */
@Controller
public class HelloController {

    @RequestMapping(value = {"/hello","/h1","h2"},method = RequestMethod.GET)
    @ResponseBody
    public String hello(){
        return "hello  world  swagger ";
    }

}
