package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Item> productList;
    private Context context;
    public MyAdapter(Context context, List<Item> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = productList.get(position);
        Log.d("MyAdapter", "Binding item at position " + position + ": " + item.getName());
        holder.productVariation.setText(item.getVariation());
        // Set quantity in the EditText
        holder.quantityEditText.setText(String.valueOf(item.getQuantity()));
        holder.productName.setText(item.getName());
        holder.productPrice.setText(item.getPrice());
        holder.productImage.setImageBitmap(item.getImage());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int userId = preferences.getInt("USER_ID", -1);
        Log.d("RemoveCartItem", "Retrieved userId: " + userId);

        // Listen for changes in the quantity EditText
        holder.quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int newQuantity = Integer.parseInt(s.toString());
                    item.setQuantity(newQuantity);
                } catch (NumberFormatException e) {
                    // Default to 1 if parsing fails
                    item.setQuantity(1);
                }
            }
        });

        // Increase quantity button
        holder.increaseQuantity.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.quantityEditText.setText(String.valueOf(newQuantity));
        });

        // Decrease quantity button
        holder.decreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                holder.quantityEditText.setText(String.valueOf(newQuantity));
            }
        });

        // Remove item
        holder.removeButton.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                // Reuse 'item' instead of redeclaring:
                int cartId = item.getCartId();
                // then proceed with your thread to remove...

                new Thread(() -> {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody body = new FormBody.Builder()
                                .add("cart_id", String.valueOf(cartId))
                                .build();
                        Log.d("removecartitem", "cart_id: " + cartId);

                        Request request = new Request.Builder()
                                .url("http://10.0.2.2/Exchange/remove_cart_item.php")
                                .post(body)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseBody = response.body().string();
                        Log.d("RemoveCartItem", "Server Response: " + responseBody);

                        if (responseBody.contains("\"status\":\"success\"")) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                productList.remove(positionToRemove);
                                notifyItemRemoved(positionToRemove);
                                notifyItemRangeChanged(positionToRemove, productList.size());
                            });
                        } else {
                            Log.e("RemoveCartItem", "Failed to remove item from server");
                        }
                    } catch (Exception e) {
                        Log.e("RemoveCartItem", "Error in API request", e);
                    }
                }).start();
            }
        });

        // Checkbox functionality
        holder.selectCheckbox.setChecked(item.isSelected());
        holder.selectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productVariation, productPrice, quantityEditText;
        Button increaseQuantity, decreaseQuantity, removeButton;
        CheckBox selectCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productVariation = itemView.findViewById(R.id.productVariation); // New ID for variation
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityEditText = itemView.findViewById(R.id.quantityedit); // New ID for EditText
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
        }
    }
}
