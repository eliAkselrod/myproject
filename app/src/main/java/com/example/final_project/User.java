package com.example.final_project;

import java.io.Serializable;

public class User implements Serializable {
    private  String name;
    private String email;
    private String password;
    long id;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        id=-1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
