package com.example.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class item_adapter extends RecyclerView.Adapter<item_adapter.ItemViewHolder> {

    private List<com.example.inventory_exchange_draft_1.item_product> itemList;

    public item_adapter(List<com.example.inventory_exchange_draft_1.item_product> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        com.example.inventory_exchange_draft_1.item_product item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(item.getPrice());
        holder.itemStock.setText(item.getStock());
        holder.itemImage.setImageResource(item.getImageResource());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView itemImage, editIcon;
        TextView itemName, itemPrice, itemStock;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemStock = itemView.findViewById(R.id.itemStock);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }
}
