package com.example.exchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class item_adapter extends RecyclerView.Adapter<item_adapter.ItemViewHolder> {

    private List<Item> itemList;
    private final Context context;

    public item_adapter(Context context, List<Item> itemList) {
        this.context = context;
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
        Item item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("₱ " +item.getPrice());
        holder.itemImage.setImageBitmap(item.getImage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlaceItem00Activity.class);

            // Convert Bitmap to ByteArray for passing the image
            Bitmap bitmap = item.getImage();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Passing data via extras
            intent.putExtra("productID", item.getProductId());
            intent.putExtra("productImage", byteArray);
            intent.putExtra("productName", item.getName());
            intent.putExtra("productPrice", item.getPrice());

            context.startActivity(intent);
        });

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
