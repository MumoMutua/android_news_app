package com.mumo.newsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mumo.newsapp.databinding.FragmentVideoFormBinding;


public class VideoFormFragment extends Fragment {

    /**
     * Binding is an easier way of accessing XML Components in Java
     */

    FragmentVideoFormBinding binding;

    public VideoFormFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentVideoFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnAddDiscover.setOnClickListener(v ->{

        });

        return root;
    }
}