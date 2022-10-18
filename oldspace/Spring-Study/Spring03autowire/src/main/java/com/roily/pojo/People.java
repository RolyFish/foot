package com.roily.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author RoilyFish
 */
public class People {

    private String name;
    @Autowired
    @Qualifier("dog1")
    private Dog dog;
    @Autowired
    private Cat cat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", dog=" + dog +
                ", cat=" + cat +
                '}';
    }
}

