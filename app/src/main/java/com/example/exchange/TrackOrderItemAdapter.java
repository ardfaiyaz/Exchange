package com.example.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrackOrderItemAdapter extends RecyclerView.Adapter<TrackOrderItemAdapter.ViewHolder> {

    private List<TrackOrderItemModel> orderItems;

    public TrackOrderItemAdapter(List<TrackOrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trackorder_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrackOrderItemModel orderItem = orderItems.get(position);

        holder.productName.setText(orderItem.getProductName());
        holder.variantName.setText(orderItem.getVariantName());
        holder.quantity.setText("Qty: " + orderItem.getQuantity());
        holder.price.setText("₱" + orderItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, variantName, quantity, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.item_name);
            variantName = itemView.findViewById(R.id.item_version);
            quantity = itemView.findViewById(R.id.item_quantity);
            price = itemView.findViewById(R.id.price);
        }
    }
}
