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

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mumo.newsapp.databinding.FragmentNewsDetailsBinding;

public class NewsDetailsFragment extends Fragment {

    FragmentNewsDetailsBinding binding;

    public NewsDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);

        if (getArguments() != null){
            String title = getArguments().getString("TITLE");
            String image = getArguments().getString("IMAGE");
            String content = getArguments().getString("CONTENT");
            String url = getArguments().getString("URL");
            String author = getArguments().getString("AUTHOR");

            binding.textNewsDetails.setText(content);
            binding.textDetailsAuthor.setText(author);
            binding.textDetailsTitle.setText(title);
            Glide.with(requireActivity()).load(image).into(binding.imageNewsDetails);

            setUrlButtonClickListener(url);

            if(((HomeActivity)getActivity()).getSupportActionBar() != null){
                ((HomeActivity)getActivity()).getSupportActionBar().setTitle(title);
            }
        }

        return binding.getRoot();
    }

    private void setUrlButtonClickListener(String url) {

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

            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_SEND, webpage);
            intent.putExtra(Intent.EXTRA_TEXT, url);
            intent.setType("text/plain");

            try {
                startActivity(Intent.createChooser(intent, null));
            }
            catch (ActivityNotFoundException e){
                Snackbar.make(binding.getRoot(), "Couldn't find an activity to open webpage", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

}