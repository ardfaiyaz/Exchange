
package com.example.exchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private Context context;
    private List<InventoryProduct> productList;

    public InventoryAdapter(Context context, List<InventoryProduct> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inventory_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryProduct product = productList.get(position);
        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText(String.valueOf(product.getProductPrice()));
        holder.itemStock.setText(String.valueOf(product.getProductStock()));
        holder.itemVarId.setText(product.getVarId());

        Log.d("AdapterDebug", "Variant ID: " + product.getVarId());

        String base64Image = product.getImageBase64();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.itemImage.setImageBitmap(decodedBitmap);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                holder.itemImage.setImageResource(R.drawable.id_it); // Default image
            }
        } else {
            holder.itemImage.setImageResource(R.drawable.id_it); // Default image if null/empty
        }

        // Enable editing on EditText fields when clicked
        holder.itemPrice.setOnClickListener(v -> {
            holder.itemPrice.setFocusableInTouchMode(true);
            holder.itemPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            holder.itemPrice.requestFocus();
        });

        holder.itemStock.setOnClickListener(v -> {
            holder.itemStock.setFocusableInTouchMode(true);
            holder.itemStock.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.itemStock.requestFocus();
        });

        // Add click listener for the edit button
        holder.editIcon.setOnClickListener(view -> {
            try {
                BigDecimal newPrice = new BigDecimal(holder.itemPrice.getText().toString());
                int newStock = Integer.parseInt(holder.itemStock.getText().toString());

                product.setProductPrice(newPrice);
                product.setProductStock(newStock);

                updateProduct(product);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid input for price or stock", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct(InventoryProduct product) {
        String url = "http://10.0.2.2/Exchange/update_product.php";

        Log.d("UPDATE_REQUEST", "Sending Update Request - Name: " + product.getProductName() +
                ", Var ID: " + product.getVarId() + ", Price: " + product.getProductPrice() +
                ", Stock: " + product.getProductStock());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("UPDATE_RESPONSE", "Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Update failed: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Invalid server response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("UPDATE_ERROR", "Error: " + error.toString());
                    Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", product.getProductName());
                params.put("var_id", product.getVarId());
                params.put("price", String.valueOf(product.getProductPrice()));
                params.put("stock", String.valueOf(product.getProductStock()));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public int getItemCount() { return productList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemVarId;
        EditText itemPrice, itemStock;
        ImageView itemImage, editIcon;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemStock = itemView.findViewById(R.id.itemStock);
            itemImage = itemView.findViewById(R.id.itemImage);
            editIcon = itemView.findViewById(R.id.editIcon);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemVarId = itemView.findViewById(R.id.itemVarId);
        }
    }
}
