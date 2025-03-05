package com.example.exchange;

import android.content.Context;
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
    private List<Item> productList;
    private Context context;
    private OnItemCheckedChangeListener listener;
    private int userId; // ✅ Corrected order

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChanged();
    }

    // ✅ FIXED: Corrected argument order
    public MyAdapter(Context context, List<Item> productList, OnItemCheckedChangeListener listener, int userId) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.userId = userId;
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
        holder.productName.setText(item.getName());
        holder.productVariation.setText(item.getVariation());
        holder.productPrice.setText(item.getPrice());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));
        holder.productImage.setImageBitmap(item.getImage());

        holder.selectCheckbox.setChecked(item.isSelected());
        holder.selectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            listener.onItemCheckedChanged();
        });

        holder.increaseQuantity.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.quantityText.setText(String.valueOf(newQuantity));

            // ✅ Call `updateCartQuantity` from `UserYourCartActivity`
            if (context instanceof UserYourCartActivity) {
                ((UserYourCartActivity) context).updateCartQuantity(item.getCartId(), newQuantity);
            }

            if (item.isSelected()) listener.onItemCheckedChanged();
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                holder.quantityText.setText(String.valueOf(newQuantity));

                // ✅ Call `updateCartQuantity` from `UserYourCartActivity`
                if (context instanceof UserYourCartActivity) {
                    ((UserYourCartActivity) context).updateCartQuantity(item.getCartId(), newQuantity);
                }

                if (item.isSelected()) listener.onItemCheckedChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productVariation, productPrice, quantityText;
        Button increaseQuantity, decreaseQuantity;
        CheckBox selectCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productVariation = itemView.findViewById(R.id.productVariation);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityText = itemView.findViewById(R.id.quantityedit);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
        }
    }
}
