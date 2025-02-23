package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Item> productList;
    private Context context;
    public MyAdapter(Context context, List<Item> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = productList.get(position);
        Log.d("MyAdapter", "Binding item at position " + position + ": " + item.getName());
        holder.productVariation.setText(item.getVariation());
        // Set quantity in the EditText
        holder.quantityEditText.setText(String.valueOf(item.getQuantity()));
        holder.productName.setText(item.getName());
        holder.productPrice.setText(item.getPrice());
        holder.productImage.setImageBitmap(item.getImage());



        // Listen for changes in the quantity EditText
        holder.quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int newQuantity = Integer.parseInt(s.toString());
                    item.setQuantity(newQuantity);
                } catch (NumberFormatException e) {
                    // Default to 1 if parsing fails
                    item.setQuantity(1);
                }
            }
        });

        // Increase quantity button
        holder.increaseQuantity.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.quantityEditText.setText(String.valueOf(newQuantity));
        });

        // Decrease quantity button
        holder.decreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                holder.quantityEditText.setText(String.valueOf(newQuantity));
            }
        });

        // Remove item
        holder.removeButton.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                // Save removed item and position for potential undo
                Item removedItem = productList.get(positionToRemove);
                productList.remove(positionToRemove);
                notifyItemRemoved(positionToRemove);
                notifyItemRangeChanged(positionToRemove, productList.size());

                // Show Snackbar with Undo action
                Snackbar.make(v, removedItem.getName() + " removed", Snackbar.LENGTH_LONG)
                        .setAction("Undo", undoView -> {
                            // Reinsert the item and notify adapter
                            productList.add(positionToRemove, removedItem);
                            notifyItemInserted(positionToRemove);
                            notifyItemRangeChanged(positionToRemove, productList.size());

                            // Optionally update persistent storage to re-add this item
                        }).show();
            }
        });

        // Checkbox functionality
        holder.selectCheckbox.setChecked(item.isSelected());
        holder.selectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productVariation, productPrice, quantityEditText;
        Button increaseQuantity, decreaseQuantity, removeButton;
        CheckBox selectCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productVariation = itemView.findViewById(R.id.productVariation); // New ID for variation
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityEditText = itemView.findViewById(R.id.quantityedit); // New ID for EditText
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
        }
    }
}
