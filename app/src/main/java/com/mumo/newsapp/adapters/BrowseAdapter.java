package com.mumo.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mumo.newsapp.Networking.pojos.Article;
import com.mumo.newsapp.R;
import com.mumo.newsapp.ui.home.HomeViewModel;

import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder>{

    List<Article> browseList;
    Context context;
    HomeViewModel homeViewModel;

    public BrowseAdapter(List<Article>  browseList, Context context) {
        this.browseList = browseList;
        this.context = context;
        this.homeViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(HomeViewModel.class);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBrowse;
        TextView textTitle;
        TextView textSub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBrowse = itemView.findViewById(R.id.imgBrowse);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSub = itemView.findViewById(R.id.textSub);

            itemView.setOnClickListener(v -> {
//                Bundle bundle = new Bundle();
//                bundle.putString("IMAGE", browseList.get(getAdapterPosition()).getUrlToImage());
//                bundle.putString("TITLE", browseList.get(getAdapterPosition()).getTitle());
//                bundle.putString("CONTENT", browseList.get(getAdapterPosition()).getContent());
//                bundle.putString("URL", browseList.get(getAdapterPosition()).getUrl());
//                bundle.putString("AUTHOR", browseList.get(getAdapterPosition()).getAuthor());

                homeViewModel.setSelectedNews(browseList.get(getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.newsDetailsFragment);

            });

        }
    }

}

