package com.mumo.newsapp.Networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Id;

public class PeopleResponse {

    public PeopleResponse() {
    }

    public PeopleResponse(Integer user_id, String username, String email) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
    }


    @SerializedName("id")
    @Expose
    private Integer user_id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
