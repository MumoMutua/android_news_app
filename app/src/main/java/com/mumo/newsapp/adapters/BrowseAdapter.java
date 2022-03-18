package com.mumo.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mumo.newsapp.R;
import com.mumo.newsapp.models.Browse;
import com.mumo.newsapp.models.Discover;

import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder>{

    List<Browse> browseList;
    Context context;

    public BrowseAdapter(List<Browse>  browseList, Context context) {
        this.browseList = browseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(browseList.get(position).getImage()).into(holder.imgBrowse);
        holder.textTitle.setText(browseList.get(position).getTextTitle());
        holder.textSub.setText(browseList.get(position).getTextSubTitle());

    }

    @Override
    public int getItemCount() { return browseList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBrowse;
        TextView textTitle;
        TextView textSub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBrowse = itemView.findViewById(R.id.imgBrowse);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSub = itemView.findViewById(R.id.textSub);
        }
    }
}

