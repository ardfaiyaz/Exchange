package com.example.exchange;


import android.content.Intent;

import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class StaffSalesReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_sales_report);

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffSalesReportActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });
    }
}
