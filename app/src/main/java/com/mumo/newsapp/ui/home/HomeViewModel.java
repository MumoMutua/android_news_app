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

    private MutableLiveData<List<Article>> articles = new MutableLiveData<>();
    private MutableLiveData<Article> selected_article = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Article>> getNews(){

        return new NewsRepo(getApplication().getApplicationContext()).getBrowseData();

    }

    public LiveData<List<Article>> displayNews() {

        articles = getNews();
        return articles;

    }
    public void setSelectedNews(Article article){
        selected_article.setValue(article);
    }
    public LiveData<Article> getSelectedNews(){
        return selected_article;
    }
}