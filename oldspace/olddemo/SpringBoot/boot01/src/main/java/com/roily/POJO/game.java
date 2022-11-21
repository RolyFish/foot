package com.roily.POJO;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author RoilyFish
 */
//@Configuration
//@EnableConfigurationProperties({game.lol.class})
public class game {

    //@ConfigurationProperties(prefix = "game.lol")
    public  class lol{
        int age;
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "lol{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Bean
    CfGame getCF(){
        return new CfGame();
    }


}
