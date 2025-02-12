package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
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

        loadProductVariants();

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

        findViewById(R.id.selectImageBtn).setOnClickListener(v -> {
            openGalleryForImage();
        });
    }

    private void loadProductVariants() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Exchange/get_product_var.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray jsonArray = new JSONArray(result.toString());
                ArrayList<String> variants = new ArrayList<>();
                HashMap<String, String> variantMap = new HashMap<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String varName = obj.getString("var_name");
                    String varId = obj.getString("var_id");

                    variants.add(varName);
                    variantMap.put(varName, varId);
                }

                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, variants);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerVar.setAdapter(adapter);

                    spinnerVar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedVarName = variants.get(position);
                            selectedVarId = variantMap.get(selectedVarName);
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedVarId = null;
                        }
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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
            imageViewProduct.setImageURI(selectedImageUri);
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

                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Product uploaded successfully", Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to upload product", Toast.LENGTH_LONG).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error occurred while uploading", Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
