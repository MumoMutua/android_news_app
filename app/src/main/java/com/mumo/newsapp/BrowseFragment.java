package com.mumo.newsapp;

import android.content.ActivityNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mumo.newsapp.Networking.ServiceGenerator;
import com.mumo.newsapp.Networking.URLs;
import com.mumo.newsapp.Networking.pojos.Article;
import com.mumo.newsapp.adapters.BrowseAdapter;
import com.mumo.newsapp.databinding.FragmentBrowseBinding;
import com.mumo.newsapp.models.Browse;
import com.mumo.newsapp.ui.home.HomeViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BrowseFragment extends Fragment {

    FragmentBrowseBinding binding;
    private BrowseAdapter browseAdapter;
    private final List<Article> articles = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private String topic = "Technology";
    SweetAlertDialog progressDialog;

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.displayNews();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBrowseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.browseRecyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.browseRecyclerview.setNestedScrollingEnabled(true);

        browseAdapter = new BrowseAdapter(articles, requireActivity());
        binding.browseRecyclerview.setAdapter(browseAdapter);


        progressDialog = new SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#5474F1"));
        progressDialog.setTitleText("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getBrowseData("Technology", toDate(), "relevance");

        binding.textInputLayout2.setEndIconOnClickListener(v -> {
            if (binding.inputSearch.getText().toString().trim().isEmpty()){
                binding.inputSearch.setError("Please enter a search topic");
                Snackbar.make(root, "Please enter a search topic", Snackbar.LENGTH_LONG).show();
            }
            else {
                topic = binding.inputSearch.getText().toString().trim();
                getBrowseData(topic, toDate(), "relevance");
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String sortBy = "relevance";
                switch (tab.getPosition()){
                    case 0:
                        sortBy = "relevance";
                        break;
                    case 1:
                        sortBy = "popularity";
                        break;
                    case 2:
                        sortBy = "publishedAt";
                        break;
                }
                getBrowseData(topic, toDate(),sortBy);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }
    public void getBrowseData(String topic, String date, String sortBy) {

        progressDialog.show();

        Call<Browse> call = ServiceGenerator.getInstance()
                .getApiConnector()
                .getNews(topic, date, sortBy, URLs.API_KEY, 100);

        call.enqueue(new Callback<Browse>() {
            @Override
            public void onResponse(Call<Browse> call, Response<Browse> response) {

               progressDialog.dismiss();

                if (response.code() == 200 && response.body() != null) {

                    articles.clear();
                    articles.addAll(response.body().getArticles());
                    browseAdapter.notifyDataSetChanged();

                } else {
                    Log.d("TEST::", "onResponse: " + response.message());
                    try {
                        Toast.makeText(requireActivity(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Browse> call, Throwable t) {

               progressDialog.dismiss();

                Log.d("TEST::", "onFailure : " + t.getMessage());
                try {
                    Toast.makeText(requireActivity(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private String toDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }
}