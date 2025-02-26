package com.example.exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private TextView userFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_profile);

        userFullName = findViewById(R.id.userprofilename);

        loadUserName();

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

    @SuppressLint("SetTextI18n")
    private void loadUserName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = preferences.getString("USER_FIRST_NAME", "");
        String lastName = preferences.getString("USER_LAST_NAME", "");
        userFullName.setText(firstName + " " + lastName); // Set full name on the profile
    }
}
