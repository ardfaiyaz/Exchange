package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffProductListingActivity extends AppCompatActivity {


    EditText etProductName, etProdPrice, etProdStock, etEmail, etPassword;
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_product_listing);

        etProductName = findViewById(R.id.productnameid);
        etProdPrice = findViewById(R.id.productpriceid);
        etProdStock = findViewById(R.id.productstockid);

        findViewById(R.id.uploadbtn).setOnClickListener(view -> {
                    String ProductName = etProductName.getText().toString().trim();
                    String ProdPrice = etProdPrice.getText().toString().trim();
                    String ProdStock = etProdStock.getText().toString().trim();
        });

            findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProductListingActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.uploadbtn).setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"Successful Uploaded", Toast.LENGTH_LONG).show();
        });
    }
}
