package com.example.exchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager; // ✅ Import this
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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

public class UserTrackOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserTrackOrdersAdapter adapter;
    private List<UserTrackOrderModel> orderList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    private static final String API_URL = "http://10.0.2.2/Exchange/user_fetch_order.php?user_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_track_orders);

        recyclerView = findViewById(R.id.usertrackorderrview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Use the same SharedPreferences file as LoginActivity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int userId = preferences.getInt("USER_ID", -1);
        Log.d("USER_ID_DEBUG", "Retrieved User ID: " + userId);

        if (userId != -1) {
            fetchOrders(userId);
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            Log.e("USER_ID_ERROR", "USER_ID not found in SharedPreferences!");
        }

        // Back button functionality
        findViewById(R.id.backbtn).setOnClickListener(view -> {
            startActivity(new Intent(UserTrackOrdersActivity.this, UserProfileActivity.class));
        });

        // Navigation buttons
        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            startActivity(new Intent(UserTrackOrdersActivity.this, UserHomePageActivity.class));
        });

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            startActivity(new Intent(UserTrackOrdersActivity.this, UserYourCartActivity.class));
        });
    }

    private void fetchOrders(int userId) {
        String url = API_URL + userId;
        Log.d("API_CALL", "Requesting URL: " + url);

        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Failed to fetch orders: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(UserTrackOrdersActivity.this, "API Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("API_RESPONSE", jsonResponse);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray ordersArray = jsonObject.getJSONArray("orders");

                        orderList.clear();

                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObj = ordersArray.getJSONObject(i);

                            int orderId = orderObj.getInt("order_id");
                            String productName = orderObj.getString("product_name");
                            String variant = orderObj.getString("variant");
                            int quantity = orderObj.getInt("quantity");
                            double totalPrice = orderObj.getDouble("total_price"); // ✅ Total order price

                            // Decode base64 image
                            Bitmap bitmap = null;
                            if (orderObj.has("product_image") && !orderObj.isNull("product_image")) {
                                String base64Image = orderObj.getString("product_image");
                                if (!base64Image.isEmpty()) {
                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                    bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }
                            }

                            orderList.add(new UserTrackOrderModel(orderId, productName, variant, quantity, totalPrice, bitmap));
                        }

                        new Handler(Looper.getMainLooper()).post(() -> {
                            adapter = new UserTrackOrdersAdapter(UserTrackOrdersActivity.this, orderList);
                            recyclerView.setAdapter(adapter);
                            Log.d("ADAPTER_DEBUG", "Adapter attached with " + orderList.size() + " items.");
                        });

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "HTTP Error: " + response.code());
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(UserTrackOrdersActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

}