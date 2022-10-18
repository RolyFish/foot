package com.camemax.dao;

import com.camemax.pojo.Users;
import org.apache.ibatis.annotations.*;

public interface UsersDao {
    /*
    * 使用注解完成CRUD操作
    * */
    @Select("select * from school.users where id = #{userId}")
    Users getUserInfoById(@Param("userId") int id);

    @Insert("insert into school.users value(#{id},#{username},#{password},#{email},#{gender})")
    int insertUserInfo(@Param("userId") int id
            ,@Param("userName") String username
            ,@Param("userPassword") String password
            ,@Param("userEmail") String email
            ,@Param("userGender") int gender
    );

    @Delete("delete from school.users where id = #{userId}")
    int deleteUserInfoById(@Param("userId") int id);

    @Update("update school.users set username = #{userName} , password = #{userPassword} , email = #{userEmail} , gender = #{userGender} where id = #{userId}")
    int updateUserInfoById(
            @Param("userId") int id
            ,@Param("userName") String username
            ,@Param("userPassword") String password
            ,@Param("userEmail") String email
            ,@Param("userGender") int gender
    );
}
