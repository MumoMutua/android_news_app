package com.mumo.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    Handler delayHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = findViewById(R.id.img_mine);
        Glide.with(this).load(R.raw.mail).into(image);

        delayHandler.postDelayed(() -> {
           startActivity(new Intent(MainActivity.this, HomeActivity.class));
           finish();
        }, 2000);

    }
}