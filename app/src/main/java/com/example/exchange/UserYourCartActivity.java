package com.example.exchange;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserYourCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> ItemList;
    private int userId;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_your_cart);

        recyclerView = findViewById(R.id.cartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPriceTextView = findViewById(R.id.totalprice);

        SharedPreferences preferences = getDefaultSharedPreferences(this);
        userId = preferences.getInt("USER_ID", -1);

        ItemList = new ArrayList<>();
        fetchCartItems(userId); // ✅ Use OkHttpClient

        findViewById(R.id.userprofilebtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));
        findViewById(R.id.userhomebtn).setOnClickListener(view -> startActivity(new Intent(this, UserHomePageActivity.class)));
        findViewById(R.id.backbtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));
        findViewById(R.id.placeorderbtn).setOnClickListener(view -> confirmOrder(userId));
    }

    // ✅ Fixed `fetchCartItems()` to use OkHttpClient
    private void fetchCartItems(int userId) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                String url = "http://10.0.2.2/Exchange/get_cart_items.php?user_id=" + userId;
                Log.d("CartFetchRequest", "Requesting: " + url);

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                Log.d("CartFetchSuccess", "Server Response: " + responseBody);

                if (response.code() != 200) {
                    Log.e("CartFetchError", "HTTP Error: " + response.code());
                    runOnUiThread(() -> Toast.makeText(this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show());
                    return;
                }

                JSONObject jsonResponse = new JSONObject(responseBody);

                if (!jsonResponse.has("status") || !jsonResponse.getString("status").equals("success")) {
                    Log.e("CartFetchError", "API Response: " + jsonResponse.toString());
                    runOnUiThread(() -> Toast.makeText(this, "No cart items found", Toast.LENGTH_SHORT).show());
                    return;
                }

                JSONArray cartArray = jsonResponse.getJSONArray("cart");
                ItemList.clear();

                for (int i = 0; i < cartArray.length(); i++) {
                    JSONObject obj = cartArray.getJSONObject(i);

                    int cartId = obj.getInt("cart_id");  // ✅ cart_id is now available
                    int productId = obj.getInt("product_id");
                    String prodName = obj.getString("prod_name");
                    double prodPrice = obj.getDouble("prod_price");
                    String varId = obj.getString("var_id");
                    int quantity = obj.getInt("quantity");

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.id_it);
                    ItemList.add(new Item(cartId, productId, prodName, "Variation: " + varId, "₱ " + prodPrice, bitmap, quantity));
                }

                runOnUiThread(() -> {
// ✅ Fix `fetchCartItems()` to match new constructor order
                    adapter = new MyAdapter(this, ItemList, this::updateTotalPrice, userId);
                    recyclerView.setAdapter(adapter);
                    updateTotalPrice();
                });

            } catch (Exception e) {
                Log.e("CartFetchError", "Error fetching cart items", e);
                runOnUiThread(() -> Toast.makeText(this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void confirmOrder(int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Order");
        builder.setMessage("Are you sure you want to place this order? Order cancellation is not allowed.");
        builder.setPositiveButton("Yes", (dialog, which) -> placeOrder(userId));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
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

    public void updateTotalPrice() {
        double total = 0;
        for (Item item : ItemList) {
            if (item.isSelected()) {
                total += Double.parseDouble(item.getPrice().replace("₱ ", "")) * item.getQuantity();
            }
        }
        final double finalTotal = total;
        runOnUiThread(() -> totalPriceTextView.setText("₱ " + finalTotal));
    }
}
