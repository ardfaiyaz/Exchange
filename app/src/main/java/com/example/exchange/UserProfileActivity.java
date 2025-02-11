package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    TextView usernameText, useremailText, userstudentidText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_profile);

        // Initialize TextViews
        usernameText = findViewById(R.id.username);
        useremailText = findViewById(R.id.useremail);
        userstudentidText = findViewById(R.id.userstudentid);

        // Get data from intent
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        int userId = getIntent().getIntExtra("user_id", -1);

        // Set data to UI
        usernameText.setText(username);
        useremailText.setText(email);
        userstudentidText.setText(String.valueOf(userId));

        // Navigation buttons
        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userdeleteacc).setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.usertrackorder).setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, UserTrackOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userorderhistory).setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, UserOrderHistoryActivity.class);
            startActivity(intent);
        });
    }
}
