package com.example.exchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserOrderHistoryAdapter orderAdapter;
    private List<UserOrderHistoryModel> orderList = new ArrayList<>();
    private static final String API_URL = "http://10.0.2.2/Exchange/fetch_user_completed_orders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order_history);

        recyclerView = findViewById(R.id.userorderhistoryrview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchUserOrders();

        findViewById(R.id.userprofilebtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));
        findViewById(R.id.userhomebtn).setOnClickListener(view -> startActivity(new Intent(this, UserHomePageActivity.class)));
        findViewById(R.id.usernotifbtn).setOnClickListener(view -> startActivity(new Intent(this, UserNotificationActivity.class)));
        findViewById(R.id.usercartbtn).setOnClickListener(view -> startActivity(new Intent(this, UserYourCartActivity.class)));
        findViewById(R.id.backbtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));// 🔄 Fetch orders immediately when activity starts
    }

    private void fetchUserOrders() {
        orderList.clear(); // Clear previous data

        // ✅ Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId == -1) {
            Log.e("USER_ERROR", "User ID not found in SharedPreferences");
            Toast.makeText(this, "Error: User not logged in!", Toast.LENGTH_LONG).show();
            return;
        }

        String url = API_URL + "?user_id=" + userId;
        Log.d("DEBUG", "Fetching orders from: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API_RESPONSE", "Response: " + response.toString());

                    try {
                        if (!response.getBoolean("success")) {
                            Log.e("API_ERROR", "API failed: " + response.getString("message"));
                            Toast.makeText(this, "No completed orders found", Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONArray orders = response.getJSONArray("orders");
                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject obj = orders.getJSONObject(i);
                            orderList.add(new UserOrderHistoryModel(
                                    obj.getString("order_id"),
                                    obj.getString("status_id"),
                                    obj.getString("prod_name"),
                                    obj.getString("prod_image"),
                                    obj.getString("var_name"),
                                    convertTo12HourFormat(obj.getString("date_completed")), // 🔄 Convert date format
                                    obj.getInt("quantity"),
                                    obj.getDouble("total_price")
                            ));
                        }

                        // ✅ Ensure Adapter is Initialized Before Updating UI
                        if (orderAdapter == null) {
                            orderAdapter = new UserOrderHistoryAdapter(this, orderList);
                            recyclerView.setAdapter(orderAdapter);
                        } else {
                            orderAdapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        Log.e("API_ERROR", "JSON Parsing Error: " + e.getMessage());
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Failed to fetch orders. Network error: " + error.toString());
                    Toast.makeText(this, "Network error: Check server connection", Toast.LENGTH_LONG).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private String convertTo12HourFormat(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateTime; // Return original if conversion fails
        }
    }
}
