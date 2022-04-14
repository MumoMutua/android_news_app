package com.mumo.newsapp.adapters;

import android.content.Context;
import android.content.Entity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mumo.newsapp.Networking.PeopleResponse;
import com.mumo.newsapp.R;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>{

    private List<PeopleResponse> people;
    private Context context;

    public PeopleAdapter(List<PeopleResponse>people, Context context){
        this.people = people;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PeopleResponse person = people.get(position);
        holder.chatName.setText(person.getUsername());
        holder.chatMessage.setText(person.getEmail());
        Glide.with(context).load("https://picsum.photos/id/" +(position+100)+"/70/70").into(holder.imageChat);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chatName, chatMessage;
        ImageView imageChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatName = itemView.findViewById(R.id.text_chat_name);
            chatMessage = itemView.findViewById(R.id.text_chat_message);
            imageChat = itemView.findViewById(R.id.image_chat);

            itemView.setOnClickListener(v -> {

                Bundle bundle = new Bundle();
                bundle.putInt("PERSON", people.get(getAdapterPosition()).getId());
                Navigation.findNavController(v).navigate(R.id.chatDetailsFragment, bundle);
            });
        }
    }
}
