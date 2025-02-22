package com.example.exchange; // Change this to your actual package name

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Link to your XML layout

        // Get VideoView from XML
        VideoView videoView = findViewById(R.id.splash_activity);

        // Set video path from res/raw
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_activity);
        videoView.setVideoURI(videoUri);

        // Start video
        videoView.start();

        // Set listener to transition when video finishes
        videoView.setOnCompletionListener(mp -> {
            Intent intent = new Intent(SplashActivity.this, UserHomePageActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity
        });
    }
}
