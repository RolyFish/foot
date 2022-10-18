package com.example.fuzz.dao;

import java.util.List;
import com.example.fuzz.pojo.User;
import org.springframework.stereotype.Repository;

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
