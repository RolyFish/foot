package com.roily.POJO;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/2/15
 */
@Configuration
@EnableConfigurationProperties(roilyfish.class)
public class RoilyfishEnableProperties {

    @Bean(name = "person01")
    public Person tts(roilyfish r){
        Person person = new Person();
        person.setName(r.getName());
        return person;
    }

}
