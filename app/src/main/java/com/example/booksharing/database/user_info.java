package com.example.booksharing.database;

import org.litepal.crud.LitePalSupport;

public class user_info extends LitePalSupport {
    private String username;
    private String password;
//    private String email;
//    private String telphone;

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

//    public String getEmail() {
//        return email;
//    }

//    public void setEmail(String email) {
//        this.email = email;
//    }

//    public String getTelphone() {
//        return telphone;
//    }
//
//    public void setTelphone(String telphone) {
//        this.telphone = telphone;
//    }
}
