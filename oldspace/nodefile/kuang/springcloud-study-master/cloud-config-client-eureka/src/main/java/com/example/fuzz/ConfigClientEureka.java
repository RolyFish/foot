package com.example.fuzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/29 11:25
 */
@RefreshScope
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientEureka {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientEureka.class, args);
    }
}
