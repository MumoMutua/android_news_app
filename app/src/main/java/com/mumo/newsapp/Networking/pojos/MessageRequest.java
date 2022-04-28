package com.mumo.newsapp.Networking.pojos;

public class MessageRequest {
    int user_to;
    String message;

    public MessageRequest(int user_to, String message) {
        this.user_to = user_to;
        this.message = message;
    }

    public int getUser_to() {
        return user_to;
    }

}
