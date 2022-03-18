package com.mumo.newsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mumo.newsapp.adapters.BrowseAdapter;
import com.mumo.newsapp.adapters.DiscoverAdapter;
import com.mumo.newsapp.databinding.FragmentHomeBinding;
import com.mumo.newsapp.models.Browse;
import com.mumo.newsapp.models.Discover;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DiscoverAdapter discoverAdapter;
    private BrowseAdapter browseAdapter;
    private RecyclerView discoverRecyclerView;
    private List<Discover> discoverList = new ArrayList<>();
    private List<Browse> browseList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerDiscover.setLayoutManager(
                new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.recyclerBrowse.setLayoutManager(
                new LinearLayoutManager(requireActivity())
        );

        discoverList.add(new Discover("https://images.unsplash.com/photo-1646911339408-2af2aaf6eb20?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDV8QkpKTXR0ZURKQTR8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        discoverList.add(new Discover("https://images.unsplash.com/photo-1638913975386-d61f0ec6500d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60"));
        discoverList.add(new Discover("https://images.unsplash.com/photo-1647496849037-3390ed7805a7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0N3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));

        discoverAdapter = new DiscoverAdapter(discoverList, getActivity());
        binding.recyclerDiscover.setAdapter(discoverAdapter);


        browseList.add(new Browse("https://images.unsplash.com/uploads/141103282695035fa1380/95cdfeef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTN8fGpvYnN8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60", "Top UI/UX Designs", "Bryan Gachiri | 3 Hrs Ago"));
        browseList.add(new Browse("https://images.unsplash.com/photo-1546776230-bb86256870ce?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fHJvYm90c3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60", "Robots. Where are we Heading?","Mikashi Guasami | Now"));
        browseList.add(new Browse("https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTl8fGpvYnN8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60","These Top Jobs are waiting for you","Jobs in Kenya | 1 Day Ago"));

        browseAdapter = new BrowseAdapter(browseList, getActivity());
        binding.recyclerBrowse.setAdapter(browseAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}