package com.example.exchange;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import android.Manifest;
import android.os.Build;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffProductListingActivity extends AppCompatActivity {

    EditText etProductName, etProdPrice, etProdStock;
    Spinner spinnerVar;
    String selectedVarId;
    ImageView imageViewProduct;
    Uri selectedImageUri;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_product_listing);

        etProductName = findViewById(R.id.productnameid);
        etProdPrice = findViewById(R.id.productpriceid);
        etProdStock = findViewById(R.id.productstockid);
        spinnerVar = findViewById(R.id.variationspinner);
        imageViewProduct = findViewById(R.id.productImageView);


        requestStoragePermission(); // 🔹 Request permissions here

        loadProductVariants();

        findViewById(R.id.selectImageBtn).setOnClickListener(v -> openGalleryForImage());


        findViewById(R.id.backbtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffProductListingActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.uploadbtn).setOnClickListener(view -> {
            String ProductName = etProductName.getText().toString().trim();
            String ProdPrice = etProdPrice.getText().toString().trim();
            String ProdStock = etProdStock.getText().toString().trim();

            if (!ProductName.isEmpty() && !ProdPrice.isEmpty() && !ProdStock.isEmpty() && selectedVarId != null && selectedImageUri != null) {
                uploadProduct(ProductName, ProdPrice, ProdStock);
            } else {
                Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.selectImageBtn).setOnClickListener(v -> openGalleryForImage());
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 101);
            }
        } else { // For Android 12 and below
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        }
    }


    private void loadProductVariants() {
        // Manually define variants
        HashMap<String, String> variantMap = new HashMap<>();
        variantMap.put("Small", "S");
        variantMap.put("Medium", "M");
        variantMap.put("Large", "L");

        ArrayList<String> variants = new ArrayList<>(variantMap.keySet());

        // Set up adapter for spinner
        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, variants);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerVar.setAdapter(adapter);

            spinnerVar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedVarName = variants.get(position);
                    selectedVarId = variantMap.get(selectedVarName); // Get the corresponding var_id
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedVarName + " (ID: " + selectedVarId + ")", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedVarId = null;
                }
            });
        });
    }


    private void openGalleryForImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    imageViewProduct.setImageBitmap(bitmap);
                    Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadProduct(String productName, String prodPrice, String prodStock) {
        new Thread(() -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("prod_name", productName)
                        .addFormDataPart("prod_price", prodPrice)
                        .addFormDataPart("prod_stock", prodStock)
                        .addFormDataPart("var_id", selectedVarId)
                        .addFormDataPart("prod_image", "product_image.jpg", RequestBody.create(MediaType.parse("image/jpeg"), byteArray))
                        .build();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2/Exchange/product_upload.php")
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                runOnUiThread(() -> {
                    try {
                        Toast.makeText(getApplicationContext(), "Server Response: " + response.body().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
