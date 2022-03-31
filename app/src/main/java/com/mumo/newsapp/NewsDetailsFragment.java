package com.mumo.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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

            binding.textNewsDetails.setText(content);
            Glide.with(requireActivity()).load(image).into(binding.imageNewsDetails);

            if(((HomeActivity)getActivity()).getSupportActionBar() != null){
                ((HomeActivity)getActivity()).getSupportActionBar().setTitle(title);
            }
        }

        return binding.getRoot();
    }
}