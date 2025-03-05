package com.example.exchange;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
    private List<InventoryProduct> originalProductList; // Stores original data
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_inventory_page);

        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        originalProductList = new ArrayList<>(); // Initialize original list

        adapter = new InventoryAdapter(this, productList);
        recyclerView.setAdapter(adapter);


        loadProducts();
        setupSearchFunctionality(); // Call search setup method
    }

    // 🔽 Add these methods BELOW onCreate 🔽

    private void setupSearchFunctionality() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Not needed, since filtering is done in real time
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        searchBar.setOnCloseListener(() -> {
            resetList();
            return false;
        });
    }

    private void filterList(String query) {
        List<InventoryProduct> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            resetList();
        } else {
            for (InventoryProduct product : originalProductList) {
                if (product.getProductName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            productList.clear();
            productList.addAll(filteredList);
            adapter.notifyDataSetChanged();
        }
    }

    private void resetList() {
        productList.clear();
        productList.addAll(originalProductList);
        adapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        String url = "http://10.0.2.2/Exchange/get_products.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            productList.clear();
                            originalProductList.clear();

                            JSONArray products = response.getJSONArray("products");

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject obj = products.getJSONObject(i);

                                String name = obj.getString("name");
                                BigDecimal price = new BigDecimal(obj.getString("price"));
                                int stock = obj.getInt("stock");
                                String imageBase64 = obj.optString("prod_image", "");
                                String varId = obj.optString("var_id", "");

                                InventoryProduct product = new InventoryProduct(name, price, stock, imageBase64, varId);
                                productList.add(product);
                                originalProductList.add(product);
                            }
                            adapter.notifyDataSetChanged();
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
