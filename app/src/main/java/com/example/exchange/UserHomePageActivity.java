package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import java.util.ArrayList;

public class UserHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_home_page);

        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        // Add images and titles to the list
        imageList.add(new SlideModel(R.drawable.lbj1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj3, ScaleTypes.CENTER_CROP));

        // Find ImageSlider and set the image list
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        findViewById(R.id.usercartbtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, UserYourCartActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.userprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.zerotwo).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, PlaceItem00Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.zeroone).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, PlaceItem00Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.zerozerp).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, PlaceItem00Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.zerofour).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, PlaceItem00Activity.class);
            startActivity(intent);
        });

        findViewById(R.id.onezero).setOnClickListener(view -> {
            Intent intent = new Intent(UserHomePageActivity.this, PlaceItem00Activity.class);
            startActivity(intent);
        });

    }
}