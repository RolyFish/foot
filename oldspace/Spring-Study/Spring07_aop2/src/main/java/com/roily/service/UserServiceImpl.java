package com.roily.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    public void add() {
        System.out.println("add");
    }

    public void del() {
        System.out.println("del");

    }

    public void update() {
        System.out.println("update");

    }

    public void query() {
        System.out.println("query");

    }
}
