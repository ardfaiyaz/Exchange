package com.example.exchange;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
        // Use the variation field instead of description
        holder.productVariation.setText("Variation: " + item.getVariation());
        holder.productPrice.setText(item.getPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlaceItem00Activity.class);
            intent.putExtra("productID", item.getProductId());  // Ensure this is set
            intent.putExtra("productName", item.getName());
            intent.putExtra("productPrice", item.getPrice());
            intent.putExtra("productImage", item.getImage()); // Assuming you have a method to get image bytes
            v.getContext().startActivity(intent);
        });

        // Display image if available
        if (item.getImage() != null) {
            holder.productImage.setImageBitmap(item.getImage());
        }

        // Set quantity in the EditText
        holder.quantityEditText.setText(String.valueOf(item.getQuantity()));

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
                itemList.remove(positionToRemove);
                notifyItemRemoved(positionToRemove);
                notifyItemRangeChanged(positionToRemove, itemList.size());
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
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productVariation, productPrice;
        EditText quantityEditText; // Changed from TextView to EditText
        Button increaseQuantity, decreaseQuantity, removeButton;
        CheckBox selectCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productVariation = itemView.findViewById(R.id.checkBox); // New ID for variation
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityEditText = itemView.findViewById(R.id.quantityedit); // New ID for EditText
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
        }
    }
}
