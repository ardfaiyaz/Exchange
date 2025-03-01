package com.example.exchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserOrderHistoryAdapter extends RecyclerView.Adapter<UserOrderHistoryAdapter.ViewHolder> {
    private Context context;
    private List<UserOrderHistoryModel> orderList;

    public UserOrderHistoryAdapter(Context context, List<UserOrderHistoryModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userorderhistory_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserOrderHistoryModel order = orderList.get(position);

        // ✅ FIXED method names to match the model class
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.orderStatus.setText(order.getOrderStatus()); // ✅ FIXED
        holder.productName.setText(order.getProductName());
        holder.variant.setText(order.getProductVar()); // ✅ FIXED
        holder.quantity.setText(order.getQuantity() + "x");
        holder.productPrice.setText("Total: P " + order.getTotalPrice());
        holder.dateCompleted.setText("Order Completed: " + order.getDateCompleted());

        // ✅ Convert Base64 image string to Bitmap for ImageView
        if (order.getProductImage() != null && !order.getProductImage().isEmpty()) {
            byte[] decodedString = Base64.decode(order.getProductImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.productImage.setImageBitmap(decodedByte);
        } else {
            holder.productImage.setImageResource(R.drawable.id_it); // ✅ Default placeholder
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, productName, variant, quantity, productPrice, dateCompleted;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderid);
            orderStatus = itemView.findViewById(R.id.orderstatus);
            productName = itemView.findViewById(R.id.productname);
            variant = itemView.findViewById(R.id.varid);
            quantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.productprice);
            dateCompleted = itemView.findViewById(R.id.orderdatecompleted);
            productImage = itemView.findViewById(R.id.productimg);
        }
    }
}
