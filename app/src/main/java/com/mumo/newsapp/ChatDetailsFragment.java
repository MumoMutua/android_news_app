package com.mumo.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.mumo.newsapp.Networking.ChatServiceGenerator;
import com.mumo.newsapp.Networking.pojos.ChatResponse;
import com.mumo.newsapp.adapters.ChatMessageAdapter;
import com.mumo.newsapp.databinding.FragmentChatDetailsBinding;
import com.mumo.newsapp.utils.PreferenceStorage;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDetailsFragment extends Fragment {

    private @NonNull FragmentChatDetailsBinding binding;
    private Context context;
    private SweetAlertDialog pDialog;
    private List<ChatResponse> chats = new ArrayList<>();
    private int their_id = 0;
    private ChatMessageAdapter chatMessageAdapter;

    public ChatDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();

        if(getArguments()!= null){
            their_id = getArguments().getInt("PERSON");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Loading...");

        chatMessageAdapter = new ChatMessageAdapter(chats, context, their_id);
        binding.chatMessageRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.chatMessageRecyclerView.setAdapter(chatMessageAdapter);
        binding.chatMessageRecyclerView.setNestedScrollingEnabled(true);
        fetchChats();
        return root;
    }
    public void fetchChats(){

        pDialog.setContentText("Fetching Chats");
        pDialog.show();

        Call<List<ChatResponse>> call = ChatServiceGenerator.getInstance()
                .getApiConnector().getChats(their_id, "Token "+ new PreferenceStorage(context).getUserToken());
        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                pDialog.dismiss();
                if (response.code() == 200){
                    chats.clear();
                    chats.addAll(response.body());
                    chatMessageAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(binding.getRoot(),"You have no chats", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
                pDialog.dismiss();
                Snackbar.make(binding.getRoot(), "Something went wrong", Snackbar.LENGTH_LONG).show();
                Log.d("TEST::", "onFailure" +t.getMessage());
            }
        });
    }
}