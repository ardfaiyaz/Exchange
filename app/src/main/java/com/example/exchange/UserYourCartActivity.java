package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class UserYourCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_your_cart);

        recyclerView = findViewById(R.id.cartrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize item list
        itemList = new ArrayList<>();
        itemList.add(new Item("Computer Society ID Lace", "2023 Version", "₱80"));
        itemList.add(new Item("Item 2 Name", "Item 2 Description", "₱100"));

        // Set up adapter
        adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Correctly initializing and setting the OnClickListener inside onCreate
        findViewById(R.id.userprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserYourCartActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.placeorderbtn).setOnClickListener(view -> {
            StyleableToast.makeText(UserYourCartActivity.this,"Successfully Placed Order", R.style.placedordertoast).show();
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
}
