package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class PlaceItem00Activity extends AppCompatActivity {

    private CheckBox[] checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.place_item00);

        // Retrieve data from Intent
        byte[] byteArray = getIntent().getByteArrayExtra("productImage");
        String productName = getIntent().getStringExtra("productName");
        double productPrice = getIntent().getDoubleExtra("productPrice", 0.0);

        // Convert byte array back to Bitmap
        Bitmap bitmap = null;
        if (byteArray != null) {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        // Set Image, Name, and Price
        ImageView imageView = findViewById(R.id.itemleimg); // Ensure you have this ImageView in place_item00.xml
        TextView nameView = findViewById(R.id.itemnamedes);
        TextView priceView = findViewById(R.id.itempricedesc);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        nameView.setText(productName);
        priceView.setText("₱ " + productPrice);

        // Checkbox setup
        CheckBox xscb = findViewById(R.id.xscb);
        CheckBox largecb = findViewById(R.id.largecb);
        CheckBox xxxlcb = findViewById(R.id.xxxlcb);
        CheckBox smallcb = findViewById(R.id.smallcb);
        CheckBox xlcb = findViewById(R.id.xlcb);
        CheckBox mediumcb = findViewById(R.id.mediumcb);
        CheckBox xxlcb = findViewById(R.id.xxlcb);

        checkBoxes = new CheckBox[]{xscb, largecb, xxxlcb, smallcb, xlcb, mediumcb, xxlcb};

        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(PlaceItem00Activity.this, UserHomePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.addtocar).setOnClickListener(view -> {
            Intent intent = new Intent(PlaceItem00Activity.this, UserYourCartActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.placeorderbtn).setOnClickListener(view -> {
            Intent intent = new Intent(PlaceItem00Activity.this, UserTrackOrdersActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
        });

        setupCheckboxListeners();
    }

    private void setupCheckboxListeners() {
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
