package com.example.fuzz.controller;

import com.example.fuzz.pojo.User;
import com.example.fuzz.service.DeptClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 13:46
 */
@RestController
public class UserController {

    @Resource
    private DeptClientService deptClientService;

    @GetMapping("/user/list")
    public List<User> userList() {
        List<User> users = deptClientService.userList();
        System.out.println(users);
        return users;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Integer id) {
        return null;
    }
}
