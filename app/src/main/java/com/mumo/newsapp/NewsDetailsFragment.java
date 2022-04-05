package com.mumo.newsapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mumo.newsapp.databinding.FragmentNewsDetailsBinding;
import com.mumo.newsapp.ui.home.HomeViewModel;

public class NewsDetailsFragment extends Fragment {

    FragmentNewsDetailsBinding binding;
    private HomeViewModel homeViewModel;

    public NewsDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);

//        if (getArguments() != null){
//            String title = getArguments().getString("TITLE");
//            String image = getArguments().getString("IMAGE");
//            String content = getArguments().getString("CONTENT");
//            String url = getArguments().getString("URL");
//            String author = getArguments().getString("AUTHOR");
//
//            binding.textNewsDetails.setText(content);
//            binding.textDetailsAuthor.setText(author);
//            binding.textDetailsTitle.setText(title);
//            Glide.with(requireActivity()).load(image).into(binding.imageNewsDetails);
//
//            setUrlButtonClickListener(url, title);
//
//            if(((HomeActivity)getActivity()).getSupportActionBar() != null){
//                ((HomeActivity)getActivity()).getSupportActionBar().setTitle(title);
//            }
//        }

        return binding.getRoot();
    }

    private void setUrlButtonClickListener(String url, String title) {

        binding.btnFullArticle.setOnClickListener(v -> {

            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e){
                Snackbar.make(binding.getRoot(), "Couldn't find an activity to open webpage", Snackbar.LENGTH_SHORT).show();
            }

        });

        binding.buttonShare.setOnClickListener(v-> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, url);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, title);

            try {
                startActivity(Intent.createChooser(intent, null));
            }
            catch (ActivityNotFoundException e){
                Snackbar.make(binding.getRoot(), "Couldn't find an activity to open webpage", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        // A view Model's observe takes two parameters: A context and a new name to represent the data being received
        homeViewModel.getSelectedNews().observe(requireActivity(), article -> {

            binding.textNewsDetails.setText(article.getContent());
            binding.textDetailsAuthor.setText(article.getAuthor());
            binding.textDetailsTitle.setText(article.getTitle());
            Glide.with(requireActivity()).load(article.getUrlToImage()).into(binding.imageNewsDetails);

            setUrlButtonClickListener(article.getUrl(), article.getTitle());

            if(((HomeActivity)getActivity()).getSupportActionBar() != null){
                ((HomeActivity)getActivity()).getSupportActionBar().setTitle(article.getTitle());
            }

        });
    }
}