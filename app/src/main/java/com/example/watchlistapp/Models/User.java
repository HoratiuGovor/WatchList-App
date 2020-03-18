package com.example.watchlistapp.Models;


public class User {
    private String userName;
    private String userTitles;
    private String userEmail;

    public User(){}
    public User(String userEmail,String userName,String titles) {
        this.userName = userName;
        this.userTitles=titles;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTitles() {
        return userTitles;
    }

    public void setUserTitles(String userTitles) {
        this.userTitles = userTitles;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
