package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserTrackOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_track_orders);

        findViewById(R.id.firstitem).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserTrackItemActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });
    }
}