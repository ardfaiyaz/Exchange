package com.example.exchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.muddz.styleabletoast.StyleableToast;

public class UserYourCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> itemList;
    private int userId; // Retrieved from SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_your_cart);

        recyclerView = findViewById(R.id.cartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);

        // Initialize the itemList
        itemList = new ArrayList<>();

        // Instead of relying solely on intent extras, we now fetch persistent cart items
        fetchCartItems();

        // Set up button click listeners (example)
        findViewById(R.id.userprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.placeorderbtn).setOnClickListener(view -> {
            StyleableToast.makeText(UserYourCartActivity.this, "Successfully Placed Order", R.style.placedordertoast).show();
        });
        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void fetchCartItems() {
        new Thread(() -> {
            try {
                // Build the URL with the user id
                URL url = new URL("http://10.0.2.2/Exchange/get_cart_items.php?user_id=" + userId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream inputStream = conn.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder jsonResult = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResult.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject response = new JSONObject(jsonResult.toString());
                if (response.getString("status").equals("success")) {
                    JSONArray cartArray = response.getJSONArray("cart");
                    itemList.clear();
                    for (int i = 0; i < cartArray.length(); i++) {
                        JSONObject obj = cartArray.getJSONObject(i);
                        int productId = obj.getInt("product_id");
                        String prodName = obj.getString("prod_name");
                        double prodPrice = obj.getDouble("prod_price");
                        String varId = obj.getString("var_id");
                        int quantity = obj.getInt("quantity");

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.id_it);
                        adapter.notifyDataSetChanged();
                    }
                    runOnUiThread(() -> {
                        if (adapter == null) {
                            adapter = new MyAdapter(itemList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "No cart items found", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
