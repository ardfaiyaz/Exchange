package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import androidx.appcompat.widget.SearchView;
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

public class UserHomePageActivity extends AppCompatActivity {

    private UserHomePageAdapter adapter;
    private RecyclerView recyclerView;
    private List<Product> productList = new ArrayList<>();
    private List<Product> originalProductList = new ArrayList<>(); // ✅ Store unmodified data
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_home_page);

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.defpicc, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.maleunif, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.femaleunif, ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        recyclerView = findViewById(R.id.staffhomepagerview);
        searchBar = findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new UserHomePageAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        new FetchProductsTask().execute("http://10.0.2.2/Exchange/homepage_rview_data.php");

        setupSearchFunctionality();

        findViewById(R.id.usercartbtn).setOnClickListener(view ->
                startActivity(new Intent(UserHomePageActivity.this, UserYourCartActivity.class)));

        findViewById(R.id.usernotifbtn).setOnClickListener(view ->
                startActivity(new Intent(UserHomePageActivity.this, UserNotificationActivity.class)));

        findViewById(R.id.userprofilebtn).setOnClickListener(view ->
                startActivity(new Intent(UserHomePageActivity.this, UserProfileActivity.class)));

        findViewById(R.id.userhomebtn).setOnClickListener(view ->
                startActivity(new Intent(UserHomePageActivity.this, UserHomePageActivity.class)));
    }

    private class FetchProductsTask extends AsyncTask<String, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(String... urls) {
            List<Product> products = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream inputStream = conn.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder json = new StringBuilder();
                while (scanner.hasNext()) {
                    json.append(scanner.nextLine());
                }
                scanner.close();

                JSONArray jsonArray = new JSONArray(json.toString());
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
                Toast.makeText(UserHomePageActivity.this, "No products found", Toast.LENGTH_SHORT).show();
            } else {
                productList.clear();
                productList.addAll(products);
                originalProductList.clear();  // ✅ Store the full list for searching
                originalProductList.addAll(products);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setupSearchFunctionality() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Not needed, filtering happens in real-time
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        searchBar.setOnCloseListener(() -> {
            resetList();
            return false;
        });
    }

    private void filterList(String query) {
        List<Product> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            resetList();
        } else {
            for (Product product : originalProductList) {  // ✅ Use the original list
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            productList.clear();
            productList.addAll(filteredList);
            adapter.notifyDataSetChanged();
        }
    }

    private void resetList() {
        productList.clear();
        productList.addAll(originalProductList);  // ✅ Restore from original list
        adapter.notifyDataSetChanged();
    }


}