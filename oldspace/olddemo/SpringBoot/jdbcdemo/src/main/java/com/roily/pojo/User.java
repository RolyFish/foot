package com.roily.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Roly_Fish
 */
@Repository
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int uid;
    String userName;
    String passWord;
}
