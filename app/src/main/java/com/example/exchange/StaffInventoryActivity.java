package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StaffInventoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<InventoryProduct> productList;
    private SearchView searchBar;
    private FloatingActionButton fabAdd; // Declare Floating Action Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_inventory_page);

        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new InventoryAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        fabAdd = findViewById(R.id.fab_add); // Initialize FAB
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffInventoryActivity.this, StaffProductListingActivity.class);
                startActivity(intent);
            }
        });

        loadProducts();
    }

    private void loadProducts() {
        String url = "http://10.0.2.2/Exchange/get_products.php"; // Ensure URL is correct

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("API_RESPONSE", "Raw JSON: " + response.toString()); // Log full response

                        boolean success = response.getBoolean("success");
                        if (success) {
                            productList.clear();
                            JSONArray products = response.getJSONArray("products");

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject obj = products.getJSONObject(i);

                                String name = obj.getString("name");
                                BigDecimal price = new BigDecimal(obj.getString("price"));
                                int stock = obj.getInt("stock");
                                String imageBase64 = obj.optString("prod_image", "");
                                String varId = obj.optString("var_id", "");

                                productList.add(new InventoryProduct(name, price, stock, imageBase64, varId));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("API_ERROR", "API returned false for success");
                        }
                    } catch (JSONException e) {
                        Log.e("JSONError", "Parsing error: " + e.getMessage());
                    }
                },
                error -> Log.e("Volley", "Error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"))
        );

        queue.add(request);
    }
}
