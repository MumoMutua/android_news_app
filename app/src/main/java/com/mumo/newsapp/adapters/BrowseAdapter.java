package com.mumo.newsapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mumo.newsapp.Networking.pojos.Article;
import com.mumo.newsapp.R;
import com.mumo.newsapp.models.Browse;
import com.mumo.newsapp.models.Discover;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder>{

    static List<Article> browseList;
    Context context;

    public BrowseAdapter(List<Article>  browseList, Context context) {
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

        Article article = browseList.get(position);
        Glide.with(context).load(article.getUrlToImage()).into(holder.imgBrowse);
        holder.textTitle.setText(article.getTitle());
        holder.textSub.setText(article.getAuthor());

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

            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("IMAGE", browseList.get(getAdapterPosition()).getUrlToImage());
                bundle.putString("TITLE", browseList.get(getAdapterPosition()).getTitle());
                bundle.putString("CONTENT", browseList.get(getAdapterPosition()).getContent());

                Navigation.findNavController(v).navigate(R.id.newsDetailsFragment, bundle);

            });

        }
    }

}

