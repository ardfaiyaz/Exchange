package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = LayoutInflater.from(context).inflate(R.layout.user_track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserTrackOrderModel order = orderList.get(position);

        holder.itemName.setText(order.getProductName());
        holder.itemVariant.setText(order.getProductSize());
        holder.itemOrderId.setText("Order ID: #" + order.getOrderId());
        holder.itemPrice.setText("₱ " + order.getProductPrice());

        // Pass order_id to UserTrackItemActivity
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
        TextView itemName, itemVariant, itemOrderId, itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemVariant = itemView.findViewById(R.id.item_version);
            itemOrderId = itemView.findViewById(R.id.order_id);
            itemPrice = itemView.findViewById(R.id.item_price);
        }
    }
}
