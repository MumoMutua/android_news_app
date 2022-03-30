package com.mumo.newsapp.repositories;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mumo.newsapp.Networking.ServiceGenerator;
import com.mumo.newsapp.Networking.URLs;
import com.mumo.newsapp.Networking.pojos.Article;
import com.mumo.newsapp.models.Browse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepo {

    Context context;
    List<Article> articles = new ArrayList<>();

    public NewsRepo(Context context) {
        this.context = context;
    }

    public List<Article> getBrowseData(){

        Call<Browse> call = ServiceGenerator.getInstance()
                .getApiConnector()
                .getNews("Technology", "2022-03-28", "popularity", URLs.API_KEY, 5);

        call.enqueue(new Callback<Browse>() {
            @Override
            public void onResponse(Call<Browse> call, Response<Browse> response) {

                if (response.code() == 200 && response.body() != null) {

                    articles = response.body().getArticles();


                } else {
                    Log.d("TEST::", "onResponse: " + response.message());
                    try {
                        Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

        @Override
        public void onFailure(Call<Browse> call, Throwable t) {

            Log.d("TEST::", "onFailure : " +t.getMessage());
            try {
                Toast.makeText(context, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            catch (ActivityNotFoundException e){
                e.printStackTrace();
            }

        }
    });
        return articles;
    }
}