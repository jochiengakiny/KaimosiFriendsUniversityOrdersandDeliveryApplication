package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GifImageView gifImageView0 = findViewById(R.id.gifImageView0);

        // Define the URL or resource ID of the GIF image
        String gifUrl = "https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExZmU0dmdiMzkxNGd1dGRtMGxzNHdsMDdmYXRxNnh4cXAwaDB0NWVhayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/WFGmQEAUwLa441kTDR/giphy.gif\n";

        // Create a Picasso target to load the GIF into the GifImageView
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(android.graphics.Bitmap bitmap, Picasso.LoadedFrom from) {
                // Load the bitmap into the GifImageView as a GifDrawable
                try {
                    gifImageView0.setImageDrawable(new GifDrawable(bitmap.getNinePatchChunk()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, android.graphics.drawable.Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(android.graphics.drawable.Drawable placeHolderDrawable) {
                // Prepare loading, if needed
            }
        };
        Button loginButton = findViewById(R.id.start);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the login activity
                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // Use Picasso to load the GIF into the custom target
        Picasso.get().load(gifUrl).into(target);

    }
}