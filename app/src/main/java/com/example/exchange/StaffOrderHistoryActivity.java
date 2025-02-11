package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class StaffOrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_order_history);

        // Correctly initializing and setting the OnClickListener inside onCreate
        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffOrderHistoryActivity.this, StaffNotificationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffOrderHistoryActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffOrderHistoryActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });
    }
}
