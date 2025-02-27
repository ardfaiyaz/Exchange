package com.example.exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import io.github.muddz.styleabletoast.StyleableToast;

public class StaffProfileActivity extends AppCompatActivity {

    private TextView staffFullName;
    private LinearLayout logoutBtn; // Logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_profile);

        staffFullName = findViewById(R.id.userprofilename); // Ensure this ID exists in staff_profile.xml
        logoutBtn = findViewById(R.id.logoutbtn); // Initialize logout button

        // Load staff name from SharedPreferences
        loadStaffName();

        // Logout button listener
        logoutBtn.setOnClickListener(view -> logoutUser());

        // Navigation buttons
        findViewById(R.id.staffnotifbtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffNotificationsActivity.class))
        );

        findViewById(R.id.staffinventorybtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffInventoryActivity.class))
        );

        findViewById(R.id.staffplbtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffProductListingActivity.class))
        );

        findViewById(R.id.stafftrackorderbtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffTrackOrdersActivity.class))
        );

        findViewById(R.id.stafforderhistorybtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffOrderHistoryActivity.class))
        );

        findViewById(R.id.staffhomebtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffHomePageActivity.class))
        );

        findViewById(R.id.staffprofilebtn).setOnClickListener(view ->
                startActivity(new Intent(StaffProfileActivity.this, StaffProfileActivity.class))
        );
    }

    @SuppressLint("SetTextI18n")
    private void loadStaffName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = preferences.getString("USER_FIRST_NAME", "");
        String lastName = preferences.getString("USER_LAST_NAME", "");
        staffFullName.setText(firstName + " " + lastName); // Set full name on the profile
    }

    private void logoutUser() {
        // Clear SharedPreferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.clear();
        editor.apply();

        // Show logout toast
        StyleableToast.makeText(StaffProfileActivity.this, "Logged Out Successfully!", R.style.placedordertoast).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(StaffProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish();
    }
}
