package com.example.fuzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/22 11:47
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaServer
public class CloudEureka9001 {

    public static void main(String[] args) {
        SpringApplication.run(CloudEureka9001.class, args);
    }

}
