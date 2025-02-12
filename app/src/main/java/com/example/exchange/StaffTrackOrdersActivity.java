package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class StaffTrackOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_track_orders);

        // Correctly initializing and setting the OnClickListener inside onCreate
        findViewById(R.id.staffprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffTrackOrdersActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffnotifbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffTrackOrdersActivity.this, StaffNotificationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffTrackOrdersActivity.this, StaffHomePageActivity.class);
            startActivity(intent);
        });

    }
}
