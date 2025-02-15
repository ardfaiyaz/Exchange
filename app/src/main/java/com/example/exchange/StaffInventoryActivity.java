package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StaffInventoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private INV_Adapter invAdapter;
    private List<InventoryClass> inventoryList;
    private static final String FETCH_PRODUCTS_URL = "http://10.0.2.2/Exchange/fetch_products.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_inventory);

        recyclerView = findViewById(R.id.inID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inventoryList = new ArrayList<>();
        invAdapter = new INV_Adapter(inventoryList);
        recyclerView.setAdapter(invAdapter);

        fetchProducts();

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffInventoryActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });
    }

    private void fetchProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, FETCH_PRODUCTS_URL, null,
                response -> {
                    try {
                        inventoryList.clear();  // Clear old data
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject product = response.getJSONObject(i);
                            String name = product.getString("prod_name");
                            double price = product.getDouble("prod_price");
                            int stock = product.getInt("prod_stock");

                            inventoryList.add(new InventoryClass(name, price, stock, false));
                        }
                        invAdapter.notifyDataSetChanged(); // Update RecyclerView
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", "Failed to fetch: " + error.toString());
                    Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

}
