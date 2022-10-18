package com.example.fuzz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 13:38
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.example.fuzz.dao"})
public class CloudProvider7002 {

    public static void main(String[] args) {
        SpringApplication.run(CloudProvider7002.class, args);
    }
}
