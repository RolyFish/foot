package com.example.fuzz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/29 10:42
 */
@RefreshScope
@RestController
public class TestController {

    @Value("${content}")
    String content;

    @GetMapping("/")
    public String home() {
        return content;
    }

}
