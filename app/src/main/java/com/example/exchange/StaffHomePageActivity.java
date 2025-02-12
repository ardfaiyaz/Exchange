package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class StaffHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_home_page);

        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        // Add images and titles to the list
        imageList.add(new SlideModel(R.drawable.lbj1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj3, ScaleTypes.CENTER_CROP));

        // Find ImageSlider and set the image list
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        findViewById(R.id.staffprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffHomePageActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffnotifbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffHomePageActivity.this, StaffNotificationsActivity.class);
            startActivity(intent);
        });


    }
}