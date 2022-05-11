package com.mumo.newsapp.repositories;

import android.util.Log;

import com.mumo.newsapp.Networking.ServiceGenerator;
import com.mumo.newsapp.Networking.pojos.ChatResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepo {
//    public void syncChats(String token){
//        Call<List<ChatResponse>> call = ServiceGenerator.getInstance()
//                .getApiConnector().syncChats("Token "+ token);
//
//        call.enqueue(new Callback<List<ChatResponse>>() {
//            @Override
//            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
//
//                Log.d("TEST::", "onFailure: "+t.getMessage());
//            }
//        });
//    }
}
