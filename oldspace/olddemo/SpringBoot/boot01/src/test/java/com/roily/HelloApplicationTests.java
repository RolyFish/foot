package com.roily;

import com.roily.POJO.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelloApplicationTests {

    @Autowired
    @Qualifier("dog")
    private Dog dog;
    @Autowired
    @Qualifier("person")
    private Person person;

    @Autowired
    @Qualifier("cat")
    private Cat cat;

    @Autowired
    private org.springframework.boot.autoconfigure.web.ResourceProperties ResourceProperties;

    @Autowired
    Person person01;

    //@Autowired
    //private game.lol lol;

    @Test
    void contextLoads() {

        System.out.println(dog.toString());
    }

    @Test
    void contextLoads2() {

        System.out.println(person.toString());
    }

    @Test
    void contextLoads3() {

        System.out.println(cat.toString());
    }

    @Test
    void contextLoads4() {

        System.out.println(ResourceProperties.getStaticLocations().getClass().getName().toString());
    }

    @Test
    void contextLoads5() {

        System.out.println(person01.getName());
    }

}
