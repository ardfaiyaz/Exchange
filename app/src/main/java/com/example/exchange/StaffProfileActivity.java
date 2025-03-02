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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StaffProfileActivity extends AppCompatActivity {

    private TextView staffFullName, totalSalesTextView;
    private LinearLayout logoutBtn; // Logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_profile);

        staffFullName = findViewById(R.id.userprofilename); // Ensure this ID exists in staff_profile.xml
        totalSalesTextView = findViewById(R.id.totalsales); // Make sure this ID exists in your XML
        logoutBtn = findViewById(R.id.logoutbtn); // Initialize logout button

        // Load staff name from SharedPreferences
        loadStaffName();

        // Fetch total sales
        getTotalSales();

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

    private void getTotalSales() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2/Exchange/get_total_sales.php?action=get_total_sales";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        if (jsonObject.getBoolean("success")) {
                            final double totalSales = jsonObject.getDouble("total_sales");

                            // Update UI on the main thread
                            runOnUiThread(() -> totalSalesTextView.setText("₱" + totalSales));
                        } else {
                            runOnUiThread(() ->
                                    {
                                        try {
                                            Toast.makeText(StaffProfileActivity.this, "Error: " + jsonObject.getString("message"), R.style.accinputerror).show();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(StaffProfileActivity.this, "Failed to fetch total sales!", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
