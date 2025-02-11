package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserOrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_order_history);

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserOrderHistoryActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserOrderHistoryActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserOrderHistoryActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });
    }
}