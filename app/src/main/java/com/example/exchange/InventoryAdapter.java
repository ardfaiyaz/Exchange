package com.example.exchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private Context context;
    private List<InventoryProduct> productList;

    public InventoryAdapter(Context context, List<InventoryProduct> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inventory_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryProduct product = productList.get(position);
        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText("₱" + product.getProductPrice());
        holder.itemStock.setText(String.valueOf(product.getProductStock()));
        holder.itemVarId.setText(product.getVarId());

        Log.d("AdapterDebug", "Variant ID: " + product.getVarId());


        String base64Image = product.getImageBase64();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.itemImage.setImageBitmap(decodedBitmap);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                holder.itemImage.setImageResource(R.drawable.id_it); // Default image
            }
        } else {
            holder.itemImage.setImageResource(R.drawable.id_it); // Default image if null/empty
        }
    }





    @Override
    public int getItemCount() { return productList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemStock, itemVarId;
        ImageView itemImage, editIcon;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemStock = itemView.findViewById(R.id.itemStock);
            itemImage = itemView.findViewById(R.id.itemImage);
            editIcon = itemView.findViewById(R.id.editIcon);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemVarId = itemView.findViewById(R.id.itemVarId);

        }
    }
}
