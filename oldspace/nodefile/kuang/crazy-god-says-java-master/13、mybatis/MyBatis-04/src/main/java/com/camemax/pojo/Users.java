package com.camemax.pojo;

public class Users {

    private int userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private int userGender;

    public Users() { };

    public Users(int id, String username, String password, String email, int gender) {
        this.userId = id;
        this.userName = username;
        this.userPassword = password;
        this.userEmail = email;
        this.userGender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + userId +
                ", username='" + userName + '\'' +
                ", password='" + userPassword + '\'' +
                ", email='" + userEmail + '\'' +
                ", gender=" + userGender +
                '}';
    }
}
