package com.example.exchange;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTrackOrderAdapter extends RecyclerView.Adapter<StaffTrackOrderAdapter.ViewHolder> {

    private Context context;
    private List<StaffTrackOrdersModel> orderList;
    private String[] orderStatusList = {"Pending", "Preparing", "Reserve", "Ready for Pick-up", "Completed"};
    private static final String API_URL = "http://10.0.2.2/Exchange/update_order_status.php"; // Replace with your server URL
    private OkHttpClient client = new OkHttpClient(); // OkHttp Client

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

        // Handle Spinner selection
        holder.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedStatus = orderStatusList[pos];
                order.setOrderStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Notify Customer Button Click Listener
        holder.notifyBtn.setOnClickListener(v -> {
            String selectedStatus = order.getOrderStatus();
            updateOrderStatus(order.getOrderId(), order.getUserId(), selectedStatus, holder.getAdapterPosition());
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
        if (status == null) return 0; // Default to "Pending" if status is null
        for (int i = 0; i < orderStatusList.length; i++) {
            if (orderStatusList[i].equalsIgnoreCase(status)) {
                return i;
            }
        }
        return 0; // Default to "Pending" if status is not found
    }

    private void updateOrderStatus(String orderId, String userId, String status, int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", orderId);
            jsonObject.put("status", status);
            jsonObject.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(API_URL).post(body).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("API_ERROR", "Failed to update order: " + e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                Log.d("API_RESPONSE", "Server Response: " + responseString);

                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(context, "Order updated!", Toast.LENGTH_SHORT).show();

                            // ✅ Remove "Completed" orders from RecyclerView
                            if ("Completed".equalsIgnoreCase(status)) {
                                orderList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, orderList.size());
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.e("JSON_ERROR", "Error parsing response: " + e.getMessage() + " | Response: " + responseString);
                }
            }
        });
    }


}
