package com.mumo.newsapp.Networking;

import com.mumo.newsapp.Networking.pojos.News;
import com.mumo.newsapp.models.Browse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Endpoints {

    @GET("everything")
    Call<Browse> getNews(
            @Query("q") String topic,
            @Query("from") String from,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey);
}
