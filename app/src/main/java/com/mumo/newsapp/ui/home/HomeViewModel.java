package com.mumo.newsapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mumo.newsapp.Networking.pojos.Article;
import com.mumo.newsapp.repositories.NewsRepo;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Article>> articles;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Article> getNews(){

        return new NewsRepo(getApplication().getApplicationContext()).getBrowseData();

    }

    public LiveData<List<Article>> displayNews() {

        if(articles == null ){
            articles = new MutableLiveData<>();
            articles.setValue(getNews());

        }
        else if (articles.getValue().size() == 0){
            getNews();
        }

        return articles;
    }
}