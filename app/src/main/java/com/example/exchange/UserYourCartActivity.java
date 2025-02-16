package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        // Receive the intent data
        Intent gettintent = getIntent();
        String productName = gettintent.getStringExtra("productName");
        double productPrice = gettintent.getDoubleExtra("productPrice", 0.0);
        byte[] byteArray = gettintent.getByteArrayExtra("productImage");

        Bitmap bitmap = null;
        if (byteArray != null) {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        // If data is received, add it to the cart list
        if (productName != null) {
            itemList.add(new Item(productName, "Size: M", "₱" + productPrice, bitmap));
        }

        // Set up the adapter with updated itemList
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
