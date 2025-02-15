package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import java.util.ArrayList;

public class PlaceItem00Activity extends AppCompatActivity {

    private CheckBox[] checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.place_item00);

        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        // Add images and titles to the list
        imageList.add(new SlideModel(R.drawable.lbj1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.lbj3, ScaleTypes.CENTER_CROP));

        // Find ImageSlider and set the image list
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        CheckBox xscb = findViewById(R.id.xscb);
        CheckBox largecb = findViewById(R.id.largecb);
        CheckBox xxxlcb = findViewById(R.id.xxxlcb);
        CheckBox smallcb = findViewById(R.id.smallcb);
        CheckBox xlcb = findViewById(R.id.xlcb);
        CheckBox mediumcb = findViewById(R.id.mediumcb);
        CheckBox xxlcb = findViewById(R.id.xxlcb);


        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(PlaceItem00Activity.this, UserHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.addtocar).setOnClickListener(view -> {
            Intent intent = new Intent(PlaceItem00Activity.this, UserYourCartActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Successful Added", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.placeorderbtn).setOnClickListener(view -> {
            Intent intent = new Intent(PlaceItem00Activity.this, UserTrackOrdersActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Successful Placed Order", Toast.LENGTH_LONG).show();
        });

        setupCheckboxListeners(xscb, largecb, xxxlcb, smallcb, xlcb, mediumcb, xxlcb);
    }

    private void setupCheckboxListeners(CheckBox xscb, CheckBox largecb, CheckBox xxxlcb, CheckBox smallcb, CheckBox xlcb, CheckBox mediumcb, CheckBox xxlcb) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (CheckBox other : checkBoxes) {
                        if (other != checkBox) {
                            other.setChecked(false);
                        }
                    }
                }
            });
        }
    }
}