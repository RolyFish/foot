package com.example.fuzz.service;

import com.example.fuzz.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/23 11:29
 */
@Component
@FeignClient(value = "SPRINGCLOUD-PROVIDER")
public interface DeptClientService {

    @GetMapping(value = "/user/list")
    List<User> userList();
}
