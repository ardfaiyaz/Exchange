package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class StaffHomePageAdapter extends RecyclerView.Adapter<StaffHomePageAdapter.ViewHolder> {

    private final List<Product> productList;
    private final Context context;

    public StaffHomePageAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_home_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.price.setText("₱ " + product.getPrice());
        holder.productImg.setImageBitmap(product.getImage()); // Set image

        // Click Listener to send data to PlaceItem00Activity
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, PlaceItem00Activity.class);
//
//            // Convert Bitmap to ByteArray
//            Bitmap bitmap = product.getImage();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            intent.putExtra("productID", product.getProductId());
//            intent.putExtra("productImage", byteArray); // Send image as byte array
//            intent.putExtra("productName", product.getName());
//            intent.putExtra("productPrice", product.getPrice());
//
//            context.startActivity(intent);
//        });
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
