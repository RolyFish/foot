package com.example.fuzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/29 11:11
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerEureka {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerEureka.class, args);
    }
}
