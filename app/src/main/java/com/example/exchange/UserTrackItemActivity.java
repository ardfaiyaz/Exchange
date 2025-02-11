package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UserTrackItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_track_item);

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackItemActivity.this, UserTrackOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackItemActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackItemActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });
    }
}