package com.example.fuzz.config;

import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 13:47
 */
@Configuration
public class BeanConfig {

//    @Bean
//    @LoadBalanced
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }
//
//    @Bean
//    public IRule myRule() {
//        return new AvailabilityFilteringRule();
//    }
}
