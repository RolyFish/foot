package com.example.fuzz.controller;

import com.example.fuzz.service.UserService;
import com.example.fuzz.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 13:35
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public List<User> selectList() {
        List<User> users = userService.selectList();
        users.add(new User(-1, "来自集群2"));
        return users;
    }

}
