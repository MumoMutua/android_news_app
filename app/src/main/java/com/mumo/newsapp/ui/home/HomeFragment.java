package com.mumo.newsapp.ui.home;

import android.app.NotificationManager;
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
    private DiscoverAdapter discoverAdapter;
    private BrowseAdapter browseAdapter;
    private RecyclerView discoverRecyclerView;
    private List<Discover> discoverList = new ArrayList<>();
    private List<Browse> browseList = new ArrayList<>();
    private Box<Discover> discoverBox = ObjectBox.get().boxFor(Discover.class);
    private List<Article> articles = new ArrayList<>();

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

        /*
        discoverList.add(new Discover("https://images.unsplash.com/photo-1646911339408-2af2aaf6eb20?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDV8QkpKTXR0ZURKQTR8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        discoverList.add(new Discover("https://images.unsplash.com/photo-1638913975386-d61f0ec6500d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60"));
        discoverList.add(new Discover("https://images.unsplash.com/photo-1647496849037-3390ed7805a7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0N3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
       */

        discoverList.clear();
        discoverList.addAll(discoverBox.getAll());
        discoverAdapter = new DiscoverAdapter(discoverList, getActivity());
        binding.recyclerDiscover.setAdapter(discoverAdapter);


        //browseList.add(new Browse("https://images.unsplash.com/uploads/141103282695035fa1380/95cdfeef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTN8fGpvYnN8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60", "Top UI/UX Designs", "Bryan Gachiri | 3 Hrs Ago"));
        //browseList.add(new Browse("https://images.unsplash.com/photo-1546776230-bb86256870ce?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fHJvYm90c3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60", "Robots. Where are we Heading?","Mikashi Guasami | Now"));
        //browseList.add(new Browse("https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTl8fGpvYnN8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60","These Top Jobs are waiting for you","Jobs in Kenya | 1 Day Ago"));

        browseAdapter = new BrowseAdapter(articles, getActivity());
        binding.recyclerBrowse.setAdapter(browseAdapter);
        binding.btnLoadMore.setOnClickListener(v -> {

            Navigation.findNavController(root).navigate(R.id.browseFragment);
        });

        getBrowseData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

//        notifications = new Notifications(getActivity());
//        notifications.createNotificationChannel();

//        notificationManagerCompat = NotificationManagerCompat.from(getActivity());
//        String title = "My Notification";
//        String text = "This is my first notification ever";
//        notificationManagerCompat.notify(243, notifications.registerNotification(title, text).build());
//        notificationManagerCompat.notify(254, notifications.bigTextNotification("Big Text Style", getActivity().getResources().getString(R.string.large_text)).build());

//        showImageNotification();

        Intent intent = new Intent(getActivity(), MyServices.class);
        getActivity().startService(intent);

    }

    private void showImageNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.notification_1540708456);
        notificationManagerCompat.notify(263, notifications.bigImageNotification("Screenshot Captured", bitmap).build());
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