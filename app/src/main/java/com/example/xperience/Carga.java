package com.example.xperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(R.drawable.video2).into(gifImageView);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(Carga.this, Inicio.class);
                startActivity(intent);
                finish();
            }
        };


        handler.postDelayed(runnable, 5000);


    }
}