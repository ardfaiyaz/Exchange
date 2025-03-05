package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class StaffOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffOrderHistoryAdapter orderAdapter;
    private List<StaffOrderHistoryModel> orderList = new ArrayList<>();
    private static final String API_URL = "http://10.0.2.2/Exchange/fetch_completed_orders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_order_history);

        recyclerView = findViewById(R.id.stafforderhistoryrview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchCompletedOrders();

        findViewById(R.id.staffprofilebtn).setOnClickListener(view -> startActivity(new Intent(this, StaffProfileActivity.class)));
        findViewById(R.id.staffhomebtn).setOnClickListener(view -> startActivity(new Intent(this, StaffHomePageActivity.class)));
        findViewById(R.id.backbtn).setOnClickListener(view -> startActivity(new Intent(this, StaffProfileActivity.class)));
    }

    private void fetchCompletedOrders() {
        orderList.clear(); // Clear old data before fetching

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    Log.d("API_RESPONSE", "Response: " + response.toString()); // Log API response

                    try {
                        if (!response.getBoolean("success")) {
                            Log.e("API_ERROR", "API failed: " + response.getString("message"));
                            Toast.makeText(this, "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONArray orders = response.getJSONArray("orders");
                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject obj = orders.getJSONObject(i);
                            orderList.add(new StaffOrderHistoryModel(
                                    obj.getString("order_id"),
                                    obj.getString("user_id"),
                                    obj.getString("customer_username"),
                                    obj.getString("prod_name"),
                                    obj.getString("prod_image"),
                                    obj.getString("var_name"),
                                    obj.getString("order_status"),
                                    convertTo12HourFormat(obj.getString("date_completed")), // 🔄 Convert date format
                                    obj.getInt("quantity"),
                                    obj.getDouble("total_price")
                            ));
                        }

                        // ✅ FIX: Ensure Adapter is Initialized Before Calling notifyDataSetChanged()
                        if (orderAdapter == null) {
                            orderAdapter = new StaffOrderHistoryAdapter(this, orderList);
                            recyclerView.setAdapter(orderAdapter);
                        } else {
                            orderAdapter.notifyDataSetChanged(); // Update UI safely
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
            return dateTime; // Return original if error occurs
        }
    }
}
