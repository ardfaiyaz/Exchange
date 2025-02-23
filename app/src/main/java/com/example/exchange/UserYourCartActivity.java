package com.example.exchange;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

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
    private List<Item> ItemList;
    private int userId; // Retrieved from SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_your_cart);

        recyclerView = findViewById(R.id.cartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences preferences = getDefaultSharedPreferences(UserYourCartActivity.this);
        System.out.println("burat" + preferences.toString());
        //String userId = preferences.getString("USER_ID", null);
        int userId = preferences.getInt("USER_ID", -1);

        System.out.println("tanga"+preferences.getString("USER_FIRST_NAME","WALA TANGINA MO"));

        // Initialize the itemList
        ItemList = new ArrayList<>();

        // Instead of relying solely on intent extras, we now fetch persistent cart items
        fetchCartItems(userId);

//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("user_id", userId)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://10.0.2.2/Exchange/get_cart_items.php")
//                .post(body)
//                .build();

//        new Thread(() -> {
//            try (Response response = client.newCall(request).execute()) {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body().string();
//                    Log.d("CartItems", "Response: " + responseBody);
//                    // Parse JSON and update UI with cart items
//                    System.out.print("SUCCES");
//                } else {
//                    System.out.print("FAIL");
//                    Log.e("CartError", "Failed to fetch cart: " + response.message());
//                }
//            } catch (Exception e) {
//                Log.e("CartException", "Error fetching cart", e);
//            }
//        }).start();

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

    private void fetchCartItems(int userId) {

        new Thread(() -> {
            try {
                // Build the URL with the user id
                System.out.println("PATRICK POGI" + userId);
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
                System.out.print("RESULT" + jsonResult);

                JSONObject response = new JSONObject(jsonResult.toString());
                if (response.getString("status").equals("success")) {
                    System.out.println("Hello");
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
                        Log.d("CartItems", "Added item: " + prodName + ", Variation: " + varId + ", Qty: " + quantity);

                    }
                    Log.d("CartItems", "Total items fetched: " + ItemList.size());
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
                    System.out.println("world");
                }
            } catch (Exception e) {
                Log.e("CartFetchError", "Error fetching cart items", e);
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(UserYourCartActivity.this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
