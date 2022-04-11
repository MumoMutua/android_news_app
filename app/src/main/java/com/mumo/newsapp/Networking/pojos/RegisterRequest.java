package com.mumo.newsapp.Networking.pojos;

public class RegisterRequest {

    String phoneNumber, username, email, password;

    public RegisterRequest(String phoneNumber, String username, String email, String password){
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public RegisterRequest(){

    }
}
