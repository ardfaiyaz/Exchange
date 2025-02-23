package com.example.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrackOrderItemAdapter extends RecyclerView.Adapter<TrackOrderItemAdapter.OrderViewHolder> {

    private List<OrderItem> orderList;

    public TrackOrderItemAdapter(List<OrderItem> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trackorder_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        holder.itemName.setText(order.getName());
        holder.itemVersion.setText(order.getVersion());
        holder.itemOrderId.setText("Order id: #" + order.getOrderId());
        holder.itemQuantity.setText(order.getQuantity() + "x");
        holder.itemPrice.setText("₱ " + order.getPrice());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemVersion, itemOrderId, itemQuantity, itemPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemName = itemView.findViewById(R.id.item_name);
            itemVersion = itemView.findViewById(R.id.item_version);
            itemOrderId = itemView.findViewById(R.id.item_order_id);
            itemPrice = itemView.findViewById(R.id.item_price);
        }
    }
}
