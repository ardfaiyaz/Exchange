package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffHomePageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffHomePageAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_home_page);

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.defpicc, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.imageslider2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.imageslider3, ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        recyclerView = findViewById(R.id.staffhomepagerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Load products from API
        new FetchProductsTask().execute("http://10.0.2.2/Exchange/homepage_rview_data.php");

        findViewById(R.id.staffprofilebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffHomePageActivity.this, StaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.staffhomebtn).setOnClickListener(view -> {
            Intent intent = new Intent(StaffHomePageActivity.this, StaffHomePageActivity.class);
            startActivity(intent);
        });
    }

    private class FetchProductsTask extends AsyncTask<String, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(String... urls) {
            List<Product> products = new ArrayList<>();
            try {
                // Fetch JSON from server
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                // Read response
                InputStream inputStream = conn.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder json = new StringBuilder();
                while (scanner.hasNext()) {
                    json.append(scanner.nextLine());
                }
                scanner.close();

                // Parse JSON
                JSONArray jsonArray = new JSONArray(json.toString());
                Log.d("API Response: ", json.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int productId = obj.getInt("product_id");
                    String name = obj.getString("prod_name");
                    double price = obj.getDouble("prod_price");

                    Bitmap bitmap = null;
                    if (obj.has("prod_image")) {
                        String base64Image = obj.getString("prod_image");
                        if (!base64Image.isEmpty()) {
                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        }
                    }
                    products.add(new Product(productId, name, price, bitmap));
                }
            } catch (Exception e) {
                Log.e("FetchProductsTask", "Error fetching data", e);
            }
            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (products.isEmpty()) {
                Toast.makeText(StaffHomePageActivity.this, "No products found", Toast.LENGTH_SHORT).show();
            } else {
                productList.addAll(products);
                adapter = new StaffHomePageAdapter(StaffHomePageActivity.this, productList);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}