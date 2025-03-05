// causing error
package com.example.exchange;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
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
    private OkHttpClient client;  // ✅ Use a single OkHttpClient instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_your_cart);

        recyclerView = findViewById(R.id.cartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPriceTextView = findViewById(R.id.totalprice);
        client = new OkHttpClient();

        SharedPreferences preferences = getDefaultSharedPreferences(this);
        userId = preferences.getInt("USER_ID", -1);

        ItemList = new ArrayList<>();
        fetchCartItems();

        findViewById(R.id.userprofilebtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));
        findViewById(R.id.userhomebtn).setOnClickListener(view -> startActivity(new Intent(this, UserHomePageActivity.class)));
        findViewById(R.id.usernotifbtn).setOnClickListener(view -> startActivity(new Intent(this, UserNotificationActivity.class)));
        findViewById(R.id.usercartbtn).setOnClickListener(view -> startActivity(new Intent(this, UserYourCartActivity.class)));
        findViewById(R.id.backbtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));
        findViewById(R.id.placeorderbtn).setOnClickListener(view -> checkStockAndConfirmOrder());
    }

    private void fetchCartItems() {
        new Thread(() -> {
            try {
                String url = "http://10.0.2.2/Exchange/get_cart_items.php?user_id=" + userId;
                Request request = new Request.Builder().url(url).get().build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful() || response.body() == null) {
                    showToast("Server error: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                if (!jsonResponse.optString("status", "").equals("success")) {
                    showToast("No cart items found");
                    return;
                }

                JSONArray cartArray = jsonResponse.getJSONArray("cart");
                ItemList.clear();

                for (int i = 0; i < cartArray.length(); i++) {
                    JSONObject obj = cartArray.getJSONObject(i);
                    ItemList.add(new Item(
                            obj.getInt("cart_id"),
                            obj.getInt("product_id"),
                            obj.getString("prod_name"),
                            obj.getString("var_id"),
                            obj.getString("prod_price"),
                            null, // No bitmap needed
                            obj.getInt("quantity")
                    ));
                }

                runOnUiThread(() -> {
                    adapter = new MyAdapter(this, ItemList, this::updateTotalPrice, this::removeCartItem,userId);
                    recyclerView.setAdapter(adapter);
                    updateTotalPrice();
                });

            } catch (Exception e) {
                showToast("Failed to fetch cart items");
                Log.e("CartFetchError", "Error fetching cart items", e);
            }
        }).start();
    }

    public void updateCartQuantity(int cartId, int quantity) {
        new Thread(() -> {
            try {
                String url = "http://10.0.2.2/Exchange/update_cart_quantity.php";

                JSONObject requestData = new JSONObject();
                requestData.put("cart_id", cartId);
                requestData.put("quantity", quantity);
                requestData.put("user_id", userId);

                Log.d("CartUpdate", "Sending Data: " + requestData.toString());

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")  // ✅ Ensure JSON is sent properly
                        .build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("CartUpdateError", "Failed to update cart. Response: " + response);
                    showToast("Failed to update cart quantity");
                    return;
                }

                String responseBody = response.body().string();
                Log.d("CartUpdate", "Server Response: " + responseBody);
                JSONObject jsonResponse = new JSONObject(responseBody);

                runOnUiThread(() -> {
                    if ("success".equals(jsonResponse.optString("status", ""))) {
                        showToast("Cart updated");
                        fetchCartItems();  // ✅ Refresh cart after update
                    } else {
                        showToast("Update failed: " + jsonResponse.optString("message", "Unknown error"));
                    }
                });

            } catch (Exception e) {
                Log.e("CartUpdateError", "Exception while updating cart quantity", e);
                showToast("Error updating cart");
            }
        }).start();
    }


    private void checkStockAndConfirmOrder() {
        new Thread(() -> {
            try {
                String url = "http://10.0.2.2/Exchange/check_stock.php";
                JSONArray orderDetails = new JSONArray();

                for (Item item : ItemList) {
                    if (item.isSelected()) {
                        JSONObject obj = new JSONObject();
                        obj.put("product_id", item.getProductId());
                        obj.put("var_id", item.getVariation());
                        obj.put("quantity", item.getQuantity());
                        orderDetails.put(obj);
                    }
                }

                JSONObject requestData = new JSONObject();
                requestData.put("order_details", orderDetails);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful() || response.body() == null) {
                    showToast("Stock check failed");
                    return;
                }

                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                boolean stockIssue = jsonResponse.optBoolean("low_stock", false);
                String orderSummary = jsonResponse.optString("summary", "No summary available.");

                runOnUiThread(() -> showConfirmationDialog(stockIssue, orderSummary));

            } catch (Exception e) {
                showToast("Stock check failed");
                Log.e("StockCheckError", "Error checking stock", e);
            }
        }).start();
    }

    private void showConfirmationDialog(boolean stockIssue, String orderSummary) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Order");

            // ✅ Prevent `TransactionTooLargeException`
            String shortSummary = orderSummary.length() > 500 ? orderSummary.substring(0, 500) + "..." : orderSummary;
            builder.setMessage(shortSummary + (stockIssue ? "\n\nSome items have low stock. Proceed with reservation?" : ""));

            builder.setPositiveButton("Yes", (dialog, which) -> placeOrder());
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void placeOrder() {
        new Thread(() -> {
            try {
                String url = "http://10.0.2.2/Exchange/place_order.php";
                JSONArray orderDetails = new JSONArray();
                double totalPrice = 0;

                for (Item item : ItemList) {
                    if (item.isSelected()) {
                        JSONObject obj = new JSONObject();
                        obj.put("prod_name", item.getName());
                        obj.put("variation", item.getVariation());
                        obj.put("quantity", item.getQuantity());
                        obj.put("price", Double.parseDouble(item.getPrice().replace("₱ ", "")));
                        orderDetails.put(obj);

                        totalPrice += item.getQuantity() * Double.parseDouble(item.getPrice().replace("₱ ", ""));
                    }
                }

                JSONObject requestData = new JSONObject();
                requestData.put("user_id", userId);
                requestData.put("order_details", orderDetails);
                requestData.put("total_price", totalPrice);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful() || response.body() == null) {
                    showToast("Order placement failed");
                    return;
                }

                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                runOnUiThread(() -> {
                    if ("success".equals(jsonResponse.optString("status", ""))) {
                        showToast("Order placed successfully!");

                        // ✅ **Force clear UI immediately**
                        ItemList.clear(); // Empty cart list
                        adapter.notifyDataSetChanged(); // Refresh RecyclerView

                        // ✅ **Fetch updated cart after 1 second**
                        new android.os.Handler().postDelayed(() -> fetchCartItems(), 1000);
                    } else {
                        showToast("Order failed: " + jsonResponse.optString("message", "Unknown error"));
                    }
                });


            } catch (Exception e) {
                showToast("Order placement failed");
                Log.e("OrderError", "Error placing order", e);
            }
        }).start();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Item item : ItemList) {
            if (item.isSelected()) {
                total += Double.parseDouble(item.getPrice().replace("₱ ", "")) * item.getQuantity();
            }
        }
        final double finalTotal = total;
        runOnUiThread(() -> totalPriceTextView.setText("₱ " + finalTotal));
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
    public void removeCartItem(int cartId) {
        new Thread(() -> {
            try {
                String url = "http://10.0.2.2/Exchange/remove_cart_items.php"; // Endpoint for removing cart item

                JSONObject requestData = new JSONObject();
                requestData.put("cart_id", cartId);

                Log.d("CartRemove", "Sending Data: " + requestData.toString());

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json") // Ensure JSON is sent properly
                        .build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("CartRemoveError", "Failed to remove item from cart. Response: " + response);
                    showToast("Failed to remove item from cart");
                    return;
                }

                String responseBody = response.body().string();
                Log.d("CartRemove", "Server Response: " + responseBody);
                JSONObject jsonResponse = new JSONObject(responseBody);

                runOnUiThread(() -> {
                    if ("success".equals(jsonResponse.optString("status", ""))) {
                        showToast("Item removed from cart");
                        fetchCartItems();  // Refresh cart after removal
                    } else {
                        showToast("Remove failed: " + jsonResponse.optString("message", "Unknown error"));
                    }
                });

            } catch (Exception e) {
                Log.e("CartRemoveError", "Exception while removing item from cart", e);
                showToast("Error removing item from cart");
            }
        }).start();
    }


}
