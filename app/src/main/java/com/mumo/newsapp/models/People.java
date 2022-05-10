package com.mumo.newsapp.models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class People {
    @Id
    long id;
    private Integer user_id;
    private String username;
    private String email;

    public People() {

    }

    public People(Integer user_id, String username, String email) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
    }


    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
