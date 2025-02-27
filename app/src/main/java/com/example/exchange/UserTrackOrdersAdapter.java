package com.example.exchange;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserTrackOrdersAdapter extends RecyclerView.Adapter<UserTrackOrdersAdapter.ViewHolder>{
    private Context context;
    private List<UserTrackOrderModel> orderList;

    public UserTrackOrdersAdapter(Context context, List<UserTrackOrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.usertrackorder_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserTrackOrderModel order = orderList.get(position);

        // Set Order Details
        holder.orderId.setText("Order No. " + order.getOrderId());
        holder.productName.setText(order.getProductName());
        holder.productSize.setText(order.getProductSize());
        holder.quantity.setText(order.getQuantity() + "x");
        holder.productPrice.setText("Total: P " + order.getProductPrice());

        // Decode and Set Image (Base64 to Bitmap)
        holder.productImage.setImageBitmap(order.getProductImage()); // ✅ Set Bitmap instead of resource ID
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  orderId, productName, productSize, quantity, productPrice;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderid);
            productName = itemView.findViewById(R.id.productname);
            productSize = itemView.findViewById(R.id.varid);
            quantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.productprice);
            productImage = itemView.findViewById(R.id.productimg);
        }
    }

}