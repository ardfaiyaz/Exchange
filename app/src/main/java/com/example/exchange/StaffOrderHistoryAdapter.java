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

public class StaffOrderHistoryAdapter extends RecyclerView.Adapter<StaffOrderHistoryAdapter.ViewHolder> {

    private Context context;
    private List<StaffOrderHistoryModel> orderList;

    public StaffOrderHistoryAdapter(Context context, List<StaffOrderHistoryModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stafforderhistory_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StaffOrderHistoryModel order = orderList.get(position);

        holder.customerId.setText("Customer ID: " + order.getUserId());  // ✅ Show user_id instead of username
        holder.orderStatus.setText(order.getOrderStatus());
        holder.productName.setText(order.getProductName());
        holder.productVar.setText(order.getProductVar());
        holder.quantity.setText(order.getQuantity() + "x");
        holder.totalPrice.setText("Total Amount: ₱ " + order.getTotalPrice());
        holder.orderDateCompleted.setText("Order Completed: " + order.getDateCompleted());
        holder.orderId.setText("Order ID: " + order.getOrderId());

        if (order.getProductImage() != null && !order.getProductImage().isEmpty()) {
            try {
                byte[] decodedImage = Base64.decode(order.getProductImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                holder.productImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.productImage.setImageResource(R.drawable.id_it); // Set default image if no image
        }
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerId, orderStatus, productName, productVar, quantity, totalPrice, orderDateCompleted, orderId;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerId = itemView.findViewById(R.id.customerid);
            orderStatus = itemView.findViewById(R.id.orderstatus);
            productName = itemView.findViewById(R.id.productname);
            productVar = itemView.findViewById(R.id.varid);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.productprice);
            orderDateCompleted = itemView.findViewById(R.id.orderdatecompleted);
            orderId = itemView.findViewById(R.id.order_id);
            productImage = itemView.findViewById(R.id.productimg);
        }
    }
}
