package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserHomePageAdapter extends RecyclerView.Adapter<UserHomePageAdapter.ViewHolder> {
    private final List<Product> productList;
    private final Context context;

    public UserHomePageAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public UserHomePageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_home_rview_layout, parent, false);
        return new UserHomePageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.price.setText("₱ " + product.getPrice());
        holder.productImg.setImageBitmap(product.getImage()); // ✅ Set Bitmap instead of resource ID

        // Click Listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlaceItem00Activity.class);

            // Convert Bitmap to ByteArray for passing the image
            Bitmap bitmap = product.getImage();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Passing data via extras
            intent.putExtra("productImage", byteArray);
            intent.putExtra("productName", product.getName());
            intent.putExtra("productPrice", product.getPrice());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;
        TextView productName, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.productimg);
            productName = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
        }
    }
}