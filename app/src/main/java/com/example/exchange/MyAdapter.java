package com.example.exchange;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

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
    private OnItemCheckedChangeListener listener;
    private int userId;

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChanged();
    }

    // ✅ Fixed Constructor
    public MyAdapter(Context context, List<Item> productList, OnItemCheckedChangeListener listener, int userId) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.userId = userId;
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
        holder.productName.setText(item.getName());
        holder.productVariation.setText(item.getVariation());
        holder.productPrice.setText(item.getPrice());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));
        holder.productImage.setImageBitmap(item.getImage());

        holder.selectCheckbox.setChecked(item.isSelected());
        holder.selectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            listener.onItemCheckedChanged();
        });

        // ✅ Increase Quantity & Update DB
        holder.increaseQuantity.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.quantityText.setText(String.valueOf(newQuantity));
            updateCartQuantity(item.getCartId(), newQuantity);
            if (item.isSelected()) listener.onItemCheckedChanged();
        });

        // ✅ Decrease Quantity & Update DB
        holder.decreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                holder.quantityText.setText(String.valueOf(newQuantity));
                updateCartQuantity(item.getCartId(), newQuantity);
                if (item.isSelected()) listener.onItemCheckedChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productVariation, productPrice, quantityText;
        Button increaseQuantity, decreaseQuantity;
        CheckBox selectCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productVariation = itemView.findViewById(R.id.productVariation);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityText = itemView.findViewById(R.id.quantityedit);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
        }
    }

    // ✅ Update Cart Quantity in Database
    private void updateCartQuantity(int cartId, int quantity) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("cart_id", String.valueOf(cartId))
                        .add("quantity", String.valueOf(quantity))
                        .add("user_id", String.valueOf(userId))
                        .build();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2/Exchange/update_cart_quantity.php")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                response.close();

                Log.d("UpdateCart", "Response: " + responseBody); // Debugging

                JSONObject jsonResponse = new JSONObject(responseBody);
                if (jsonResponse.getString("status").equals("success")) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Update failed: " + jsonResponse.optString("message"), Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (IOException e) {
                Log.e("UpdateCartError", "Error updating cart", e);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Network error. Try again.", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e("UpdateCartError", "Unexpected error", e);
            }
        }).start();
    }
}
