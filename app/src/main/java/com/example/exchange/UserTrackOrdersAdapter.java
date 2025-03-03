package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserTrackOrdersAdapter extends RecyclerView.Adapter<UserTrackOrdersAdapter.ViewHolder> {
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

        holder.orderId.setText("Order No. " + order.getOrderId());
        holder.productName.setText(order.getProductName());
        holder.variant.setText(order.getProductSize());
        holder.quantity.setText(order.getQuantity() + "x");
        holder.productPrice.setText("Total Order Price: P " + order.getProductPrice());

        // Set product image if available
        if (order.getProductImage() != null) {
            holder.productImage.setImageBitmap(order.getProductImage());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserTrackItemActivity.class);
            intent.putExtra("order_id", order.getOrderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, productName, variant, quantity, productPrice;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderid);
            productName = itemView.findViewById(R.id.productname);
            variant = itemView.findViewById(R.id.productsize);
            quantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.productprice);
            productImage = itemView.findViewById(R.id.productimg);
        }
    }
}