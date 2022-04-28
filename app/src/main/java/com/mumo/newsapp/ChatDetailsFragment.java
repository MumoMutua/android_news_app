package com.mumo.newsapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mumo.newsapp.Networking.ChatServiceGenerator;
import com.mumo.newsapp.Networking.pojos.ChatResponse;
import com.mumo.newsapp.Networking.pojos.MessageRequest;
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

    private FragmentChatDetailsBinding binding;
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

       // ActionBar actionBar = getActivity().getActionBar();
       // actionBar.setTitle(MessageRequest.class.newInstance().getUser_to());

        if(getArguments()!= null){
            their_id = getArguments().getInt("PERSON");
            String name = getArguments().getString("NAME");

            if (((HomeActivity) context).getSupportActionBar() != null){
                ((HomeActivity) context).getSupportActionBar().setTitle(name);
            }
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

        binding.fabSend.setOnClickListener(v -> {
            if(binding.inputMessage.getText().toString().trim().isEmpty()){
                Toast.makeText(context,"Cannot Send empty message", Toast.LENGTH_SHORT).show();
            }
            else {
                ChatResponse chatResponse = new ChatResponse();
                chatResponse.setUserFrom(new PreferenceStorage(context).getUserId());
                chatResponse.setUserTo(their_id);
                chatResponse.setUserFromName(new PreferenceStorage(context).getUserName());
                chatResponse.setMessage(binding.inputMessage.getText().toString().trim());
                chats.add(chatResponse);
                chatMessageAdapter.notifyItemInserted(chatMessageAdapter.getItemCount());

                MessageRequest messageRequest = new MessageRequest(their_id, binding.inputMessage.getText().toString().trim());
                sendMessage(messageRequest);
                binding.inputMessage.getText().clear();
                Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show();
            }

        });
        return root;
    }

    private void sendMessage(MessageRequest messageRequest) {
        Call<List<ChatResponse>> call = ChatServiceGenerator.getInstance()
                .getApiConnector().sendMessage(messageRequest,"Token " + new PreferenceStorage(context).getUserToken());

        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                if(response.code() == 200 && response.body() != null){
                    chats.clear();
                    chats.addAll(response.body());
                    chatMessageAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(binding.getRoot(), "You have no chats", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                Log.d("TEST::", "onFailure: " +t.getMessage());
            }
        });
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
                if (response.code() == 200 && response.body()!= null){
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