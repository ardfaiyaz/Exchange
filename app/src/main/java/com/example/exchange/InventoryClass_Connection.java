package com.example.exchange;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private com.example.exchange.item_adapter itemAdapter;
    private List<com.example.exchange.item_product> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Remove underline from SearchView
        SearchView searchView = findViewById(R.id.search_bar);
        if (searchView != null) {
            View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate);
            View submitArea = searchView.findViewById(androidx.appcompat.R.id.submit_area);

            if (searchPlate != null) {
                searchPlate.setBackgroundColor(Color.TRANSPARENT);
            }

            if (submitArea != null) {
                submitArea.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample Data
        itemList = new ArrayList<>();
        itemList.add(new com.example.exchange.item_product("ITEM #1 LACE BSIT", "₱80.00", "92/100", R.drawable.item_id_lace));
        itemList.add(new com.example.exchange.item_product("ITEM #2 KEYCHAIN", "₱50.00", "50/100", R.drawable.item_id_lace));
        itemList.add(new com.example.exchange.item_product("ITEM #3 STICKER", "₱20.00", "75/100", R.drawable.item_id_lace));

        // Set adapter
        itemAdapter = new com.example.exchange.item_adapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        // Button to add new product (manually for now)
        /*Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> addNewProduct());*/
    }

    private void addNewProduct() {
        // Add a new item dynamically
        itemList.add(new com.example.exchange.item_product("New ITEM", "₱100.00", "20/50", R.drawable.item_id_lace));

        // Notify the adapter that data has changed
        itemAdapter.notifyItemInserted(itemList.size() - 1);
    }
}
