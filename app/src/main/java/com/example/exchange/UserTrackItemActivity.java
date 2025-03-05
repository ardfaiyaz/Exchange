package com.example.exchange;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserTrackItemActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrackOrderItemAdapter adapter;
    private List<TrackOrderItemModel> orderItemList;

    private TextView statushead, statusfoot, totalPriceTextView;
    private View rescl, proccl, pickcl, pendcl, compcl, one, two, three, four; // Ensure this matches the actual layout

    private static final String FETCH_URL = "http://10.0.2.2/exchange/fetch_order_items.php?order_id=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_track_item);

        recyclerView = findViewById(R.id.usertrackitemrview);
        statushead = findViewById(R.id.statushead);
        statusfoot = findViewById(R.id.statusfoot);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        one = findViewById(R.id.one);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        rescl = findViewById(R.id.rescl);
        proccl = findViewById(R.id.proccl);
        pickcl = findViewById(R.id.pickcl);
        pendcl = findViewById(R.id.pendcl);
        compcl = findViewById(R.id.compcl);// Ensure this ID exists in user_track_item.xml

        if (one == null) {
            Log.e("UserTrackItemActivity", "Error: View 'one' not found in layout!");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemList = new ArrayList<>();
        adapter = new TrackOrderItemAdapter(orderItemList);
        recyclerView.setAdapter(adapter);

        int orderId = getIntent().getIntExtra("order_id", 0);

        if (orderId > 0) {
            new FetchOrderItemsTask().execute(FETCH_URL + orderId);
        } else {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            startActivity(new Intent(UserTrackItemActivity.this, UserTrackOrdersActivity.class));
        });
        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            startActivity(new Intent(UserTrackItemActivity.this, UserHomePageActivity.class));
        });
        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
                startActivity(new Intent(UserTrackItemActivity.this, UserYourCartActivity.class));
        });
        findViewById(R.id.userprofilebtn).setOnClickListener(view ->{
            startActivity((new Intent(UserTrackItemActivity.this, UserProfileActivity.class)));
        });
        findViewById(R.id.usernotifbtn).setOnClickListener(view ->{
            startActivity((new Intent(UserTrackItemActivity.this, UserNotificationActivity.class)));
        });
    }

    private class FetchOrderItemsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000); // Timeout to avoid app freezing
                connection.setReadTimeout(5000);

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                return result.toString();
            } catch (Exception e) {
                Log.e("FetchOrderItemsTask", "Error fetching data", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(UserTrackItemActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                orderItemList.clear();
                String statusId = "";

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int productId = jsonObject.getInt("product_id");
                    String prodName = jsonObject.getString("prod_name");
                    String varName = jsonObject.getString("var_name");
                    int quantity = jsonObject.getInt("quantity");
                    double price = jsonObject.getDouble("price_at_purchase");
                    statusId = jsonObject.optString("status_id", "");

                    orderItemList.add(new TrackOrderItemModel(productId, prodName, varName, quantity, price));
                }
                adapter.notifyDataSetChanged();
                updateTotalPrice();


                Log.d("FetchOrderItemsTask", "Status ID: " + statusId);

                if ("RES".equals(statusId)) {
                    rescl.setBackgroundResource(R.drawable.circle_shape);
                    statushead.setText("Order Reserved!");
                    statusfoot.setText("Great news! Your order has been reserved!");
                } else if ("PROC".equals(statusId)) {
                    statushead.setText("Order Processing");
                    statusfoot.setText("Hooray! Your order is officially in the works!");
                    rescl.setBackgroundResource(R.drawable.circle_shape);
                    proccl.setBackgroundResource(R.drawable.circle_shape);
                    one.setBackgroundColor(Color.parseColor("#ff914d"));
                } else if ("PICK".equals(statusId)) {
                    statushead.setText("Order Ready for Pickup");
                    statusfoot.setText("Awesome! Your order is ready for pickup!");
                    rescl.setBackgroundResource(R.drawable.circle_shape);
                    proccl.setBackgroundResource(R.drawable.circle_shape);
                    pickcl.setBackgroundResource(R.drawable.circle_shape);
                    one.setBackgroundColor(Color.parseColor("#ff914d"));
                    two.setBackgroundColor(Color.parseColor("#ff914d"));
                } else if ("PEND".equals(statusId)) {
                    statushead.setText("Order Pending");
                    statusfoot.setText("Hang tight! Your order is pending.");
                    rescl.setBackgroundResource(R.drawable.circle_shape);
                    proccl.setBackgroundResource(R.drawable.circle_shape);
                    pickcl.setBackgroundResource(R.drawable.circle_shape);
                    pendcl.setBackgroundResource(R.drawable.circle_shape);
                    one.setBackgroundColor(Color.parseColor("#ff914d"));
                    two.setBackgroundColor(Color.parseColor("#ff914d"));
                    three.setBackgroundColor(Color.parseColor("#ff914d"));
                } else if ("COMP".equals(statusId)) {
                    statushead.setText("Order Complete");
                    statusfoot.setText("Woohoo! Your order is complete!");
                    rescl.setBackgroundResource(R.drawable.circle_shape);
                    proccl.setBackgroundResource(R.drawable.circle_shape);
                    pickcl.setBackgroundResource(R.drawable.circle_shape);
                    pendcl.setBackgroundResource(R.drawable.circle_shape);
                    compcl.setBackgroundResource(R.drawable.circle_shape);
                    one.setBackgroundColor(Color.parseColor("#ff914d"));
                    two.setBackgroundColor(Color.parseColor("#ff914d"));
                    three.setBackgroundColor(Color.parseColor("#ff914d"));
                    four.setBackgroundColor(Color.parseColor("#ff914d"));
                } else {
                    statushead.setText("Not Found");
                    statusfoot.setText("404");
                    rescl.setBackgroundResource(R.drawable.circle_shape_unactive);
                    proccl.setBackgroundResource(R.drawable.circle_shape_unactive);
                    pickcl.setBackgroundResource(R.drawable.circle_shape_unactive);
                    pendcl.setBackgroundResource(R.drawable.circle_shape_unactive);
                    compcl.setBackgroundResource(R.drawable.circle_shape_unactive);
                    one.setBackgroundColor(Color.parseColor("#a5a3a3"));
                    two.setBackgroundColor(Color.parseColor("#a5a3a3"));
                    three.setBackgroundColor(Color.parseColor("#a5a3a3"));
                    four.setBackgroundColor(Color.parseColor("#a5a3a3"));
                }
            } catch (JSONException e) {
                Log.e("FetchOrderItemsTask", "JSON Parsing error", e);
                Toast.makeText(UserTrackItemActivity.this, "Data parsing error", Toast.LENGTH_SHORT).show();
            }

        }   private void updateTotalPrice() {
            double total = 0;
            for (TrackOrderItemModel item : orderItemList) {
                total += item.getQuantity() * item.getPrice();
            }

            String formattedTotal = String.format("%.2f", total);
            totalPriceTextView.setText(formattedTotal);

        }
    }
}
