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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.OkHttpClient;

public class PlaceItem00Activity extends AppCompatActivity {

    private CheckBox[] checkBoxes;
    private String productName;
    private double productPrice;
    private byte[] byteArray;
    private int userId;
    private int productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.place_item00);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("USER_ID", -1);
        if (userId == -1) {
            StyleableToast.makeText(this, "User not logged in", R.style.accinputerror).show();
            finish();
        }

        byteArray = getIntent().getByteArrayExtra("productImage");
        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getDoubleExtra("productPrice", 0.0);
        productID = getIntent().getIntExtra("productID", -1);

        Bitmap bitmap = null;
        if (byteArray != null) {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        ImageView imageView = findViewById(R.id.itemleimg);
        TextView nameView = findViewById(R.id.itemnamedes);
        TextView priceView = findViewById(R.id.itempricedesc);
        EditText quantityEdit = findViewById(R.id.quantityedit);
        LinearLayout placeOrderBtn = findViewById(R.id.placeorderbtn);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        nameView.setText(productName);
        priceView.setText("₱ " + productPrice);

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
            String selectedVarId = getSelectedSize();  // This returns the variation (var_id)
            int quantity = 1; // default quantity
            try {
                quantity = Integer.parseInt(quantityEdit.getText().toString().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                StyleableToast.makeText(getApplicationContext(), "Invalid quantity", R.style.accinputerror).show();
                return;
            }

            // Encode var_id
            String encodedVarId;
            try {
                encodedVarId = URLEncoder.encode(selectedVarId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                encodedVarId = selectedVarId;
            }

            // Build POST data
            String postData = "user_id=" + userId +
                    "&product_id=" + productID +
                    "&var_id=" + encodedVarId +
                    "&quantity=" + quantity;

            new Thread(() -> {
                try {
                    OkHttpClient client = new OkHttpClient();
                    okhttp3.RequestBody body = okhttp3.RequestBody.create(
                            postData,
                            okhttp3.MediaType.parse("application/x-www-form-urlencoded")
                    );
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://10.0.2.2/Exchange/add_to_cart.php")
                            .post(body)
                            .build();

                    okhttp3.Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    runOnUiThread(() -> {
                        StyleableToast.makeText(PlaceItem00Activity.this, "Item added to cart!", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to the cart activity:
                        Intent intent = new Intent(PlaceItem00Activity.this, UserYourCartActivity.class);
                        startActivity(intent);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(PlaceItem00Activity.this, "Failed to add item", Toast.LENGTH_SHORT).show());
                }
            }).start();

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Exchange/place_order.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "user_id=" + userId +
                        "&productID=" + productID +
                        "&productPrice=" + productPrice +
                        "&selectedSize=" + URLEncoder.encode(selectedSize, "UTF-8") +
                        "&quantity=" + quantityInt +
                        "&date_ordered=" + URLEncoder.encode(currentDate, "UTF-8") +
                        "&date_updated=" + URLEncoder.encode(currentDate, "UTF-8");

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
