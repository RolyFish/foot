package com.roily.config;

import com.roily.pojo.Address;
import com.roily.pojo.User1;
import com.roily.pojo.User2;
import com.roily.pojo.User3;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Roly_Fish
 */
@Configuration
//@ComponentScan("com.roily.pojo")
//@Import(JavaConfig2.class)

//@Import(User2.class)
public class JavaConfig {

    @Bean
    Address Address(){
        return new Address("滨海");
    }

    @Bean(autowire = Autowire.BY_TYPE)
    User3 User3(){
       return new User3();
    }
}
