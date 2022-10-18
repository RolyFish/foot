package com.example.fuzz.dao;

import com.example.fuzz.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuzz
 * @since 2020/10/19 13:24
 */
@Repository
public interface UserDao {
    List<User> selectList();
}
