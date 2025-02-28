package com.example.exchange;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UserNotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserNotificationAdapter adapter;
    private List<UserNotificationClass> notificationList;
    private static final String API_URL = "http://10.0.2.2/Exchange/fetch_notifications.php";
    private static final String USER_ID = "2023123456"; // Replace with actual user_id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_notifications);

        recyclerView = findViewById(R.id.orderupdatesrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();
        adapter = new UserNotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        fetchNotifications();
    }

    private void fetchNotifications() {
        RequestQueue queue = Volley.newRequestQueue(this);

        // ✅ Ensure user_id is passed correctly
        String USER_ID = "2023123456";  // Change this to the logged-in user's ID
        String url = "http://10.0.2.2/Exchange/fetch_notifications.php?user_id=" + USER_ID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API_RESPONSE", "Response: " + response.toString());

                    try {
                        boolean success = response.getBoolean("success");
                        if (!success) {
                            Toast.makeText(UserNotificationActivity.this, "No notifications found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray notificationsArray = response.getJSONArray("notifications");
                        notificationList.clear();

                        for (int i = 0; i < notificationsArray.length(); i++) {
                            JSONObject obj = notificationsArray.getJSONObject(i);
                            int id = obj.getInt("notification_id");
                            String message = obj.getString("message");
                            String date = obj.getString("notif_date");

                            notificationList.add(new UserNotificationClass(id, message, date));
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                        Toast.makeText(UserNotificationActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Volley Error", "Error: " + error.toString());
                    Toast.makeText(UserNotificationActivity.this, "Failed to fetch notifications", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }



}
