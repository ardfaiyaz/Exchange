package com.example.exchange;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StaffTrackOrderAdapter extends RecyclerView.Adapter<StaffTrackOrderAdapter.ViewHolder> {

    private Context context;
    private List<StaffTrackOrdersModel> orderList;
    private String[] orderStatusList = {"Pending", "Preparing", "Reserve", "Ready for Pick-up", "Completed"};

    public StaffTrackOrderAdapter(Context context, List<StaffTrackOrdersModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stafftrackorder_rview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StaffTrackOrdersModel order = orderList.get(position);

        // Set Order Details
        holder.customerId.setText("Customer ID: " + order.getUserId());
        holder.orderId.setText("Order No. " + order.getOrderId());
        holder.productName.setText(order.getProductName());
        holder.variant.setText(order.getVariant());
        holder.quantity.setText(order.getQuantity() + "x");
        holder.productPrice.setText("Total: P " + order.getPrice());

        // Decode and Set Image (Base64 to Bitmap)
        try {
            byte[] decodedImage = Base64.decode(order.getProductImage(), Base64.DEFAULT);
            holder.productImage.setImageBitmap(android.graphics.BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length));
        } catch (Exception e) {
            holder.productImage.setImageResource(R.drawable.id_it);  // Fallback image
        }

        // Set Spinner Adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, orderStatusList);
        holder.statusSpinner.setAdapter(spinnerAdapter);

        // Set selected item to current status
        int statusIndex = getStatusIndex(order.getOrderStatus());
        if (statusIndex != -1) {
            holder.statusSpinner.setSelection(statusIndex);
        }

        // Notify Customer Button Click Listener
        holder.notifyBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Notifying customer " + order.getUserId(), Toast.LENGTH_SHORT).show();

        });

        // Handle Spinner selection
        holder.statusSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
                String selectedStatus = orderStatusList[pos];
                order.setOrderStatus(selectedStatus);
                Toast.makeText(context, "Order " + order.getOrderId() + " updated to " + selectedStatus, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerId, orderId, productName, variant, quantity, productPrice;
        ImageView productImage;
        Spinner statusSpinner;
        View notifyBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerId = itemView.findViewById(R.id.customerid);
            orderId = itemView.findViewById(R.id.orderid);
            productName = itemView.findViewById(R.id.productname);
            variant = itemView.findViewById(R.id.varid);
            quantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.productprice);
            productImage = itemView.findViewById(R.id.productimg);
            statusSpinner = itemView.findViewById(R.id.statusspinner);
            notifyBtn = itemView.findViewById(R.id.notifybtn);
        }
    }

    private int getStatusIndex(String status) {
        for (int i = 0; i < orderStatusList.length; i++) {
            if (orderStatusList[i].equalsIgnoreCase(status)) {
                return i;
            }
        }
        return -1;
    }
}
