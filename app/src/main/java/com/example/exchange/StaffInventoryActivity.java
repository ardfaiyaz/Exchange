package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StaffInventoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private INV_Adapter invAdapter;
    private List<InventoryClass> inventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_inventory);

        recyclerView = findViewById(R.id.inID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        inventoryList = new ArrayList<>();
        inventoryList.add(new InventoryClass("ID Lace", 45000, 5, false));
        inventoryList.add(new InventoryClass("Uniform", 25000, 10, false));
        inventoryList.add(new InventoryClass("Trad", 2500, 15, false));

        invAdapter = new INV_Adapter(inventoryList);
        recyclerView.setAdapter(invAdapter);
        


    }
}
