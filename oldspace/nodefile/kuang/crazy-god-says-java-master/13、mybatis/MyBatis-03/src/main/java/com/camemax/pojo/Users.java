package com.camemax.pojo;

import org.apache.ibatis.type.Alias;

@Alias("MyBatisAliasUsers")
public class Users {

    private int id;
    private String username;
    private String password;
    private String email;
    private int gender;

    public Users() { };

    public Users(int id, String username, String password, String email, int gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                '}';
    }
}
