package com.mumo.newsapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mumo.newsapp.R;
import com.mumo.newsapp.models.Discover;
import com.mumo.newsapp.utils.CustomDialog;

import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder>{

    List<Discover> discoverList;
    Context context;
    FloatingActionButton playVideo;

    public DiscoverAdapter(List<Discover> discoverList, Context context) {
        this.discoverList = discoverList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(discoverList.get(position).getImage()).into(holder.imgDiscover);
    }

    @Override
    public int getItemCount() {
        return discoverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDiscover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDiscover = itemView.findViewById(R.id.img_discover);
            playVideo = itemView.findViewById(R.id.btn_discover_play);

            playVideo.setOnClickListener(v ->{
                String url = discoverList.get(getAdapterPosition()).getVideo_url();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            });

            itemView.setOnLongClickListener(v -> {
                Discover discover = discoverList.get(getAdapterPosition());
                new CustomDialog(context, v).showDiscoverDialog(discover);

                return true;
            });
        }
    }
}
