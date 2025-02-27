package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StaffTrackOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffTrackOrderAdapter adapter;
    private List<StaffTrackOrdersModel> orderList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "http://10.0.2.2/Exchange/fetch_orders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_track_orders);

        recyclerView = findViewById(R.id.stafftrackordersrview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list
        adapter = new StaffTrackOrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);

        // Fetch orders on activity start
        fetchOrders();

        // Navigation Listeners
        findViewById(R.id.staffprofilebtn).setOnClickListener(view -> startActivity(new Intent(this, StaffProfileActivity.class)));
        findViewById(R.id.staffnotifbtn).setOnClickListener(view -> startActivity(new Intent(this, StaffNotificationsActivity.class)));
        findViewById(R.id.staffhomebtn).setOnClickListener(view -> startActivity(new Intent(this, StaffHomePageActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchOrders();  // Refresh orders when returning to this activity
    }

    private void fetchOrders() {
        Request request = new Request.Builder().url(API_URL).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Failed to fetch orders: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(StaffTrackOrdersActivity.this, "API Error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("API_RESPONSE", jsonResponse);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        if (!jsonObject.getBoolean("success")) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(StaffTrackOrdersActivity.this, "No orders found", Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }

                        JSONArray ordersArray = jsonObject.getJSONArray("orders");

                        // Clear previous data
                        orderList.clear();

                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObj = ordersArray.getJSONObject(i);

                            String orderId = orderObj.getString("order_id");
                            String userId = orderObj.getString("user_id");
                            String productId = orderObj.getString("product_id");
                            String productName = orderObj.getString("product_name");
                            String productImage = orderObj.getString("product_image");
                            String variant = orderObj.getString("variant");
                            int quantity = orderObj.getInt("quantity");
                            double price = orderObj.getDouble("price");
                            String orderStatus = orderObj.getString("order_status");

                            // ✅ Only add non-"Completed" orders to the list
                            if (!"Completed".equalsIgnoreCase(orderStatus)) {
                                orderList.add(new StaffTrackOrdersModel(userId, orderId, productId, productName, productImage, variant, quantity, price, orderStatus));
                            }
                        }

                        // ✅ Update UI on the main thread
                        new Handler(Looper.getMainLooper()).post(() -> {
                            adapter.notifyDataSetChanged(); // Refresh RecyclerView
                        });

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(StaffTrackOrdersActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
