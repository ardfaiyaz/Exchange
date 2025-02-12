package com.example.exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class StaffProfileActivity extends AppCompatActivity {


    private TextView staffFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_profile);

        staffFullName = findViewById(R.id.userprofilename); // Ensure this ID exists in staff_profile.xml

        // Load staff name from SharedPreferences
        loadStaffName();

        // Correctly initializing and setting the OnClickListener inside onCreate
        findViewById(R.id.staffnotifbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffNotificationsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffinventorybtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffInventoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffplbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffProductListingActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.stafftrackorderbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffTrackOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.stafforderhistorybtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffOrderHistoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProfileActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadStaffName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = preferences.getString("USER_FIRST_NAME", "");
        String lastName = preferences.getString("USER_LAST_NAME", "");
        staffFullName.setText(firstName + " " + lastName); // Set full name on the profile
    }


}
