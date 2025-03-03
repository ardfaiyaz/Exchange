package com.example.exchange;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
    private View one;

    private static final String FETCH_URL = "http://10.0.2.2/exchange/fetch_order_items.php?order_id=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_track_item);

        recyclerView = findViewById(R.id.usertrackitemrview);
        one = findViewById(R.id.one); // Replace with actual view ID

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
    }

    private class FetchOrderItemsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

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

                if ("COMP".equals(statusId)) {
                    one.setBackgroundColor(Color.parseColor("#FF914D"));
                } else {
                    one.setBackgroundColor(Color.WHITE);
                }

            } catch (JSONException e) {
                Log.e("FetchOrderItemsTask", "JSON Parsing error", e);
                Toast.makeText(UserTrackItemActivity.this, "Data parsing error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
