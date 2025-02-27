package com.example.exchange;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
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

public class UserTrackOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserTrackOrdersAdapter adapter;
    private static int userId;
    private List<UserTrackOrderModel> orderList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "http://10.0.2.2/Exchange/user_fetch_order.php" + userId;// Replace with your actual URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_track_orders);

        recyclerView = findViewById(R.id.usertrackorderrview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getDefaultSharedPreferences(this);
        userId = preferences.getInt("USER_ID", -1);

        fetchOrders(userId);

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackOrdersActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });
    }
    private void fetchOrders(int userId) {
        // Dynamically build the URL with userId parameter
        String urlWithUserId = "http://10.0.2.2/Exchange/user_fetch_order.php?user_id=" + userId;

        Request request = new Request.Builder().url(urlWithUserId).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Failed to fetch orders: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(UserTrackOrdersActivity.this, "API Error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("API_RESPONSE", jsonResponse);

                    try {
                        // Parse JSON
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray ordersArray = jsonObject.getJSONArray("orders");

                        // Clear previous data
                        orderList.clear();

                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObj = ordersArray.getJSONObject(i);

                            int orderId = orderObj.getInt("order_id");
                            String productName = orderObj.getString("prod_name");
                            String productSize = orderObj.getString("size");
                            int quantity = orderObj.getInt("quantity");
                            double productPrice = orderObj.getDouble("total_price");

                            // Get base64 image data safely
                            Bitmap bitmap = null;
                            // Check if the prod_image exists; if not, set a default bitmap or leave null.
                            if(orderObj.has("prod_image")){
                                String base64Image = orderObj.getString("prod_image");
                                if(!base64Image.isEmpty()){
                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                    bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }
                            }

                            // Add the order to the list
                            orderList.add(new UserTrackOrderModel(orderId, productName, productSize, quantity, productPrice, bitmap));
                        }

                        // Update UI on the main thread
                        new Handler(Looper.getMainLooper()).post(() -> {
                            adapter = new UserTrackOrdersAdapter(UserTrackOrdersActivity.this, orderList);
                            recyclerView.setAdapter(adapter);
                        });

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(UserTrackOrdersActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                    );
                }
            }

        });
    }
}
