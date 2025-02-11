package com.example.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final List<Item> itemList;

    public MyAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);

        // Bind data to views
        holder.productName.setText(item.getName());
        holder.productDescription.setText(item.getDescription());
        holder.productPrice.setText(item.getPrice());

        // Set default quantity
        holder.quantityText.setText("1");

        // Increase quantity
        holder.increaseQuantity.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityText.getText().toString());
            holder.quantityText.setText(String.valueOf(currentQuantity + 1));
        });

        // Decrease quantity
        holder.decreaseQuantity.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityText.getText().toString());
            if (currentQuantity > 1) {
                holder.quantityText.setText(String.valueOf(currentQuantity - 1));
            }
        });

        // Remove item
        holder.removeButton.setOnClickListener(v -> {
            itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());
        });

        // Checkbox functionality
        holder.selectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Optional: Add logic for checkbox handling
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDescription, productPrice, quantityText;
        Button increaseQuantity, decreaseQuantity, removeButton;
        CheckBox selectCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityText = itemView.findViewById(R.id.quantityText);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
        }
    }
}
