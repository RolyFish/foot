package com.example.fuzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 11:46
 */
@SpringBootApplication
@EnableEurekaClient
public class CloudCustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudCustomerApplication.class, args);
    }
}
