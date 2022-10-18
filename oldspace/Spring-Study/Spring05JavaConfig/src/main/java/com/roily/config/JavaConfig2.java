package com.roily.config;


import com.roily.pojo.User2;
import com.roily.pojo.User3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Roly_Fish
 */
@Configuration
//@ComponentScan("com.roily.pojo")
public class JavaConfig2 {

    @Bean
    public User3 getUser3(){
        return new User3();
    }

    @Bean
    public User2 getUser2(){
        User2 user2 = new User2();
        user2.setName("JavaConfig2");
        return user2;
    }
}
