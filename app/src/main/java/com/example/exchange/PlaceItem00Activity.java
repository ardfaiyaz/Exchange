package com.example.exchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import io.github.muddz.styleabletoast.StyleableToast;

public class PlaceItem00Activity extends AppCompatActivity {

    private CheckBox[] checkBoxes;
    private String productName;
    private double productPrice;
    private byte[] byteArray;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.place_item00);

        // Retrieve user ID from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("USER_ID", -1);
        if (userId == -1) {
            StyleableToast.makeText(this, "User not logged in", R.style.accinputerror).show();
            finish();
        }

        // Retrieve data from Intent
        byteArray = getIntent().getByteArrayExtra("productImage");
        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getDoubleExtra("productPrice", 0.0);

        // Convert byte array back to Bitmap
        Bitmap bitmap = null;
        if (byteArray != null) {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        // Set Image, Name, and Price
        ImageView imageView = findViewById(R.id.itemleimg);
        TextView nameView = findViewById(R.id.itemnamedes);
        TextView priceView = findViewById(R.id.itempricedesc);
        EditText quantityEdit = findViewById(R.id.quantityedit);
        Button placeOrderBtn = findViewById(R.id.placeorderbtn);

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

        findViewById(R.id.addtocartbtn).setOnClickListener(view -> {
            String selectedSize = getSelectedSize();
            Intent intent = new Intent(PlaceItem00Activity.this, UserYourCartActivity.class);
            intent.putExtra("productImage", byteArray);
            intent.putExtra("productName", productName);
            intent.putExtra("productPrice", productPrice);
            intent.putExtra("selectedSize", selectedSize);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            StyleableToast.makeText(getApplicationContext(), "Successfully Added", R.style.placedordertoast).show();
        });

        placeOrderBtn.setOnClickListener(view -> placeOrder(quantityEdit));

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

    private String getSelectedSize() {
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                return cb.getText().toString();
            }
        }
        return "";
    }

    private void placeOrder(EditText quantityEdit) {
        String selectedSize = getSelectedSize();
        String quantity = quantityEdit.getText().toString().trim();

        if (selectedSize.isEmpty()) {
            StyleableToast.makeText(getApplicationContext(), "Please select a size", R.style.accinputerror).show();
            return;
        }

        if (quantity.isEmpty()) {
            StyleableToast.makeText(getApplicationContext(), "Please enter quantity", R.style.accinputerror).show();
            return;
        }

        int quantityInt = Integer.parseInt(quantity);
        if (quantityInt <= 0) {
            StyleableToast.makeText(getApplicationContext(), "Quantity must be at least 1", R.style.accinputerror).show();
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Exchange/place_order.php"); // Update with actual path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "user_id=" + userId +
                        "&productName=" + URLEncoder.encode(productName, "UTF-8") +
                        "&productPrice=" + productPrice +
                        "&selectedSize=" + URLEncoder.encode(selectedSize, "UTF-8") +
                        "&quantity=" + quantityInt;

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        StyleableToast.makeText(getApplicationContext(), "Order Placed Successfully", R.style.placedordertoast).show();
                        Intent intent = new Intent(PlaceItem00Activity.this, UserHomePageActivity.class);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> StyleableToast.makeText(getApplicationContext(), "Failed to place order", R.style.accinputerror).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> StyleableToast.makeText(getApplicationContext(), "Error: " + e.getMessage(), R.style.accinputerror).show());
            }
        }).start();
    }
}
