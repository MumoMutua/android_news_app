package com.mumo.newsapp.ui.dashboard;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mumo.newsapp.ObjectBox;
import com.mumo.newsapp.R;
import com.mumo.newsapp.adapters.DiscoverAdapter;
import com.mumo.newsapp.databinding.FragmentDashboardBinding;
import com.mumo.newsapp.models.Discover;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DiscoverAdapter discoverAdapter;
    private RecyclerView recyclerView;
    private List<Discover> discoverList = new ArrayList<>();
    private Box<Discover> discoverBox = ObjectBox.get().boxFor(Discover.class);
    MediaPlayer mediaPlayer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerDiscover.setLayoutManager(
                new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        );

        discoverList.clear();
        discoverList.addAll(discoverBox.getAll());
        discoverAdapter = new DiscoverAdapter(discoverList, getActivity());
        binding.recyclerDiscover.setAdapter(discoverAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.song);
        mediaPlayer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}