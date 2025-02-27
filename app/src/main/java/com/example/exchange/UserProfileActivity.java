package com.example.exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private TextView staffFullName, usemail, userid;
    private LinearLayout logoutBtn; // Add reference for logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_profile);

        staffFullName = findViewById(R.id.userprofilename);
        usemail = findViewById(R.id.useremail);
        userid = findViewById(R.id.userstudentid);
        logoutBtn = findViewById(R.id.logoutbtn); // Initialize logout button

        // Load user details
        loadStaffName();

        // Logout action
        logoutBtn.setOnClickListener(view -> logoutUser());

        // Navigation buttons
        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, UserYourCartActivity.class));
        });

        findViewById(R.id.userdeleteacc).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, StaffProfileActivity.class));
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, UserHomePageActivity.class));
        });

        findViewById(R.id.usertrackorder).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, UserTrackOrdersActivity.class));
        });

        findViewById(R.id.userorderhistory).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, UserOrderHistoryActivity.class));
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadStaffName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = preferences.getString("USER_FIRST_NAME", "");
        String lastName = preferences.getString("USER_LAST_NAME", "");
        int userId = preferences.getInt("USER_ID", -1);
        String eemail = preferences.getString("USER_EMAIL", "");

        staffFullName.setText(firstName + " " + lastName);
        usemail.setText(eemail);
        userid.setText(String.valueOf(userId));
    }

    private void logoutUser() {
        // Clear SharedPreferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish();
    }
}
