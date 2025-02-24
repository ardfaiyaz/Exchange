package com.example.exchange;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserYourCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> ItemList;
    private int userId;
    private Button placeOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_your_cart);

        recyclerView = findViewById(R.id.cartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getDefaultSharedPreferences(UserYourCartActivity.this);
        int userId = preferences.getInt("USER_ID", -1);

        ItemList = new ArrayList<>();
        fetchCartItems(userId);

        findViewById(R.id.userprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.placeorderbtn).setOnClickListener(view -> placeOrder(userId));
    }

    private void fetchCartItems(int userId) {
        new Thread(() -> {
            try {
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
                    ItemList.clear();

                    for (int i = 0; i < cartArray.length(); i++) {
                        JSONObject obj = cartArray.getJSONObject(i);
                        int productId = obj.getInt("product_id");
                        String prodName = obj.getString("prod_name");
                        double prodPrice = obj.getDouble("prod_price");
                        String varId = obj.getString("var_id");
                        int quantity = obj.getInt("quantity");

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.id_it);
                        ItemList.add(new Item(prodName, "Variation: " + varId, "₱ " + prodPrice, bitmap, quantity));
                    }
                    runOnUiThread(() -> {
                        if (adapter == null) {
                            adapter = new MyAdapter(UserYourCartActivity.this, ItemList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "No cart items found", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e("CartFetchError", "Error fetching cart items", e);
                runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void placeOrder(int userId) {
        new Thread(() -> {
            try {
                JSONArray orderDetailsArray = new JSONArray();
                double totalPrice = 0; // Initialize total price

                for (Item item : ItemList) {
                    if (item.isSelected()) {
                        JSONObject orderDetail = new JSONObject();
                        orderDetail.put("prod_name", item.getName());
                        orderDetail.put("variation", item.getVariation().replace("Variation: ", ""));
                        orderDetail.put("quantity", item.getQuantity());
                        orderDetail.put("price", item.getPrice().replace("₱ ", ""));
                        orderDetailsArray.put(orderDetail);

                        // Add price * quantity to totalPrice
                        totalPrice += Double.parseDouble(item.getPrice().replace("₱ ", "")) * item.getQuantity();
                    }
                }

                if (orderDetailsArray.length() == 0) {
                    runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "No items selected", Toast.LENGTH_SHORT).show());
                    return;
                }

                JSONObject orderData = new JSONObject();
                orderData.put("user_id", userId);
                orderData.put("total_price", totalPrice); // ADD THIS LINE
                orderData.put("order_details", orderDetailsArray);

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(orderData.toString(), okhttp3.MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/Exchange/place_order.php")
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();

                String responseBody = response.body().string();
                JSONObject responseJson = new JSONObject(responseBody);
                if (responseJson.getString("status").equals("success")) {
                    runOnUiThread(() -> StyleableToast.makeText(UserYourCartActivity.this, "Successfully Placed Order", R.style.placedordertoast).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "Order failed", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e("OrderError", "Error placing order", e);
                runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "Error placing order", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}
