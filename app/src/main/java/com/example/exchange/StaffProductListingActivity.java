package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class StaffProductListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_product_listing);

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProductListingActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.uploadbtn).setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"Successful Uploaded", Toast.LENGTH_LONG).show();
        });
    }
}
