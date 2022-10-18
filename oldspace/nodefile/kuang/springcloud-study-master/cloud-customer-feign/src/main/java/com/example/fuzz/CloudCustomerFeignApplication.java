package com.example.fuzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 11:46
 */
@SpringBootApplication
@EnableFeignClients
public class CloudCustomerFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudCustomerFeignApplication.class, args);
    }
}
