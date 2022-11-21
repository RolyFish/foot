package com.roily.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@PropertySource(value = "classpath:bean.yaml")
@Validated
public class Cat {
    @Value("${name}")
    private String name;

    private int age;
}
