package com.example.exchange;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



// assuming that this java is for homepage listing
public class FetchProducts extends AsyncTask<Void, Void, List<Product>> {
    private static final String URL_PHP = "http://10.0.2.2/Exchange/homepage_rview_data.php";
    private ProductListener listener;

    public FetchProducts(ProductListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {
        List<Product> productList = new ArrayList<>();
        try {
            URL url = new URL(URL_PHP);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonResult = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonResult.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int productId = obj.getInt("productId");
                String name = obj.getString("prod_name");
                double price = obj.getDouble("prod_price");
                String base64Image = obj.getString("prod_image");
                int quantity = obj.getInt("quantity");

                // Convert Base64 to Bitmap
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // ✅ FIX: Use productList instead of products
                productList.add(new Product(productId, name, price, bitmap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    protected void onPostExecute(List<Product> productList) {
        listener.onProductsFetched(productList);
    }

    public interface ProductListener {
        void onProductsFetched(List<Product> products);
    }
}
