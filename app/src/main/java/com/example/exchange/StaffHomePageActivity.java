package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class StaffHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_home_page);

        // Set up Image Slider
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.lbj1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj3, ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.staffhomepagerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Create Product List (Replacing Strings with Product Objects)
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Product 1", 199.99, R.drawable.lbj1));
        productList.add(new Product("Product 2", 299.99, R.drawable.lbj1));
        productList.add(new Product("Product 3", 399.99, R.drawable.lbj2));
        productList.add(new Product("Product 4", 499.99, R.drawable.lbj2));
        productList.add(new Product("Product 5", 599.99, R.drawable.lbj3));
        productList.add(new Product("Product 6", 699.99, R.drawable.lbj3));

        // Pass the Product List to the Adapter
        StaffHomePageAdapter adapter = new StaffHomePageAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // Button Listeners
        findViewById(R.id.staffprofilebtn).setOnClickListener(view ->
                startActivity(new Intent(StaffHomePageActivity.this, StaffProfileActivity.class))
        );

        findViewById(R.id.staffnotifbtn).setOnClickListener(view ->
                startActivity(new Intent(StaffHomePageActivity.this, StaffNotificationsActivity.class))
        );

        findViewById(R.id.staffhomebtn).setOnClickListener(view ->
                startActivity(new Intent(StaffHomePageActivity.this, StaffHomePageActivity.class))
        );
    }
}
