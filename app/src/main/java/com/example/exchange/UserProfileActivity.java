package com.example.exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

import io.github.muddz.styleabletoast.StyleableToast;

public class UserProfileActivity extends AppCompatActivity {
    private TextView staffFullName, usemail, userid;
    private LinearLayout logoutBtn, deleteAccBtn; // Add reference for logout button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_profile);

        staffFullName = findViewById(R.id.userprofilename);
        usemail = findViewById(R.id.useremail);
        userid = findViewById(R.id.userstudentid);
        logoutBtn = findViewById(R.id.logoutbtn); // Initialize logout button
        deleteAccBtn = findViewById(R.id.userdeleteacc);

        // Load user details
        loadStaffName();

        // Logout action
        logoutBtn.setOnClickListener(view -> logoutUser());

        deleteAccBtn.setOnClickListener(view -> confirmDeleteAccount());

        // Navigation buttons
        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, UserYourCartActivity.class));
        });

        findViewById(R.id.usernotifbtn).setOnClickListener(view -> {
            startActivity(new Intent(UserProfileActivity.this, UserNotificationActivity.class));
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

        StyleableToast.makeText(UserProfileActivity.this, "Logged Out Successfully!", R.style.placedordertoast).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish();
    }
    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action is irreversible.")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void deleteAccount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("USER_ID", -1);

        if (userId == -1) {
            StyleableToast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user_id", String.valueOf(userId))
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2/Exchange/delete_account.php") // Ensure this URL is correct
                .post(body)
                .build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseBody = response.body().string();
                Log.d("DeleteAccount", "Server Response: " + responseBody); // Log response for debugging

                JSONObject jsonResponse = new JSONObject(responseBody);

                // Ensure the response contains "status"
                if (!jsonResponse.has("status")) {
                    runOnUiThread(() -> StyleableToast.makeText(this, "Invalid server response!", Toast.LENGTH_SHORT).show());
                    return;
                }

                String status = jsonResponse.getString("status");

                runOnUiThread(() -> {
                    if ("success".equals(status)) {
                        StyleableToast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                        logoutUser();
                    } else {
                        StyleableToast.makeText(this, "Failed to delete account!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e("DeleteAccountError", "Error: " + e.getMessage());
                runOnUiThread(() -> {
                    StyleableToast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


}
