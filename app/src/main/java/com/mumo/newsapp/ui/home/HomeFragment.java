package com.mumo.newsapp.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mumo.newsapp.Networking.ServiceGenerator;
import com.mumo.newsapp.Networking.URLs;
import com.mumo.newsapp.Networking.pojos.Article;
import com.mumo.newsapp.ObjectBox;
import com.mumo.newsapp.R;
import com.mumo.newsapp.adapters.BrowseAdapter;
import com.mumo.newsapp.adapters.DiscoverAdapter;
import com.mumo.newsapp.databinding.FragmentHomeBinding;
import com.mumo.newsapp.models.Browse;
import com.mumo.newsapp.models.Discover;
import com.mumo.newsapp.utils.MyServices;
import com.mumo.newsapp.utils.Notifications;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    NotificationManagerCompat notificationManagerCompat;
    Notifications notifications;
    private FragmentHomeBinding binding;
    private BrowseAdapter browseAdapter;
    private RecyclerView discoverRecyclerView;
    private final List<Discover> discoverList = new ArrayList<>();
    private final List<Browse> browseList = new ArrayList<>();
    private final Box<Discover> discoverBox = ObjectBox.get().boxFor(Discover.class);
    private final List<Article> articles = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerDiscover.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerBrowse.setLayoutManager(new LinearLayoutManager(requireActivity()));

        discoverList.clear();
        discoverList.addAll(discoverBox.getAll());
        DiscoverAdapter discoverAdapter = new DiscoverAdapter(discoverList, getActivity());
        binding.recyclerDiscover.setAdapter(discoverAdapter);


        browseAdapter = new BrowseAdapter(articles, getActivity());
        binding.recyclerBrowse.setAdapter(browseAdapter);
        binding.btnLoadMore.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.browseFragment));

        getBrowseData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(getActivity(), MyServices.class);
        getActivity().startService(intent);

    }

    private void showImageNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.notification_1540708456);
        //notificationManagerCompat.notify(263, notifications.bigImageNotification("Screenshot Captured", bitmap).build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void getBrowseData(){

        Call<Browse> call = ServiceGenerator.getInstance()
                .getApiConnector()
                .getNews("Technology", "2022-03-28", "popularity", URLs.API_KEY, 5);

        call.enqueue(new Callback<Browse>() {
            @Override
            public void onResponse(Call<Browse> call, Response<Browse> response) {

                if(response.code() == 200 && response.body()!= null){

                    articles.clear();
                    articles.addAll(response.body().getArticles());
                    browseAdapter.notifyDataSetChanged();

                }
                else{
                    Log.d("TEST::", "onResponse: " + response.message());
                    try {
                        Toast.makeText(requireActivity(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                    catch (ActivityNotFoundException e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Browse> call, Throwable t) {

                Log.d("TEST::", "onFailure : " +t.getMessage());
                try {
                    Toast.makeText(requireActivity(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
                catch (ActivityNotFoundException e){
                    e.printStackTrace();
                }

            }
        });
    }
}