package com.example.fuzz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 11:50
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    private Integer id;
    private String name;
}
