package com.roily.POJO;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author RoilyFish
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Repository
//@ConfigurationProperties(prefix = "person")
@Validated
public class Person {

    /**
     * @Email(message="邮箱格式错误")
     * @NotNull(message="不为空")
     * name
     */
    private String name;
    private int age;
    private Boolean flag;

    private Date birth;
    private Map<String,String> map;
    private List<Object> list;
    private Dog dog;
    private String preName;

}
