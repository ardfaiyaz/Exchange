package com.example.exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserTrackItemActivity extends AppCompatActivity {

    private RecyclerView userTrackRecyclerView;
    private TrackOrderItemAdapter adapter;
    private List<TrackOrderItemModel> orderList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_track_item);

        // Initialize RecyclerView
        userTrackRecyclerView = findViewById(R.id.usertrackitemrview);
        userTrackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load Order Items
        loadOrderItems();

        // Set Adapter
        adapter = new TrackOrderItemAdapter(orderList);
        userTrackRecyclerView.setAdapter(adapter);

        // Button Click Listeners
        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackItemActivity.this, UserTrackOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackItemActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserTrackItemActivity.this, UserHomePageActivity.class);
            startActivity(intent);
        });
    }

    private void loadOrderItems() {
        orderList = new ArrayList<>();

        // Example Orders (Replace with Data from Database)
        orderList.add(new TrackOrderItemModel("Computer Society ID Lace", "2023 Version", "02020", 1, 80.0));
        orderList.add(new TrackOrderItemModel("Lorem ipsum", "2023 Version", "02020", 1, 80.0));
        orderList.add(new TrackOrderItemModel("Lorem ipsum", "2023 Version", "02020", 1, 80.0));
        orderList.add(new TrackOrderItemModel("Lorem ipsum", "2023 Version", "02020", 1, 80.0));
    }
}
