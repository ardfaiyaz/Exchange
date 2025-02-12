package com.example.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class INV_Adapter extends RecyclerView.Adapter<INV_Adapter.InventoryViewHolder> {

    private List<InventoryClass> inventoryList;

    public INV_Adapter(List<InventoryClass> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_rview_layout, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryClass inventoryItem = inventoryList.get(position);
        holder.textViewName.setText(inventoryItem.getName());
        holder.textViewPrice.setText("₱" + inventoryItem.getPrice());
        holder.textViewStock.setText(inventoryItem.getStock() + " pcs");
        holder.checkBox.setChecked(inventoryItem.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            inventoryItem.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textViewName, textViewPrice, textViewStock;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textViewName = itemView.findViewById(R.id.itemname_inventory);
            textViewPrice = itemView.findViewById(R.id.pricetag_inventory);
            textViewStock = itemView.findViewById(R.id.stocks_inventory);
        }
    }
}
