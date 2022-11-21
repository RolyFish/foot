package com.roily.config;

import com.roily.POJO.Person;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/13
 */
@Configuration
public class myConfig {

    @ConfigurationProperties(prefix = "person")
    @Bean(name = "person")
    public Person Person(){
        Person person = new Person();
        person.setName("config");
        return person;
    }
}
