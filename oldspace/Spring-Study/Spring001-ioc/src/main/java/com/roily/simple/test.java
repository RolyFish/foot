package com.roily.simple;

import com.roily.simple.service.UserService;
import com.roily.simple.service.UserServiceImpl;
import org.junit.Test;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/9
 */
public class test {

    @Test
    public  void t1() {
        UserService userService = new UserServiceImpl();
        userService.getuser();
    }
}
