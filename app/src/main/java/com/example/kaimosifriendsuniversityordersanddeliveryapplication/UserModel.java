package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

public class UserModel {
    String id, email, password, date, role;


    public UserModel() {
    }

    public UserModel(String id, String email, String password, String date, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.date = date;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return date;
    }

    public String getRole() {
        return role;
    }
}
