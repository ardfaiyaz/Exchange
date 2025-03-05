package com.example.exchange;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.JsonArrayRequest;
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
    private static int userId;
    private List<UserNotificationClass> notificationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_notifications); // Make sure this XML file exists

        recyclerView = findViewById(R.id.orderupdatesrv); // Ensure ID matches your XML file
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the userId from SharedPreferences
        SharedPreferences preferences = getDefaultSharedPreferences(this);//test
        userId = preferences.getInt("USER_ID", -1); // Default to -1 if not found

        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return; // Exit if userId is not found
        }

        notificationList = new ArrayList<>();
        adapter = new UserNotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        // Fetch notifications
        fetchNotifications(userId);//test

        findViewById(R.id.userprofilebtn).setOnClickListener(view -> startActivity(new Intent(this, UserProfileActivity.class)));
        findViewById(R.id.userhomebtn).setOnClickListener(view -> startActivity(new Intent(this, UserHomePageActivity.class)));
        findViewById(R.id.usernotifbtn).setOnClickListener(view -> startActivity(new Intent(this, UserNotificationActivity.class)));
        findViewById(R.id.usercartbtn).setOnClickListener(view -> startActivity(new Intent(this, UserYourCartActivity.class)));
    }

    private void fetchNotifications(int userId) {
        // Construct the API URL with the correct userId
        String apiUrl = "http://10.0.2.2/Exchange/fetch_notification.php?user_id=" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        notificationList.clear();
                        try {
                            // Log response to debug
                            Log.d("ServerResponse", response.toString());

                            // Check if response has "success" and it's true
                            if (response.has("success") && response.getBoolean("success")) {

                                // Check if response has "notifications"
                                if (response.has("notifications")) {
                                    JSONArray notificationsArray = response.getJSONArray("notifications");

                                    for (int i = 0; i < notificationsArray.length(); i++) {
                                        JSONObject obj = notificationsArray.getJSONObject(i);

                                        int id = obj.has("notification_id") ? obj.getInt("notification_id") : -1;
                                        String message = obj.has("message") ? obj.getString("message") : "No message";
                                        String date = obj.has("notif_date") ? obj.getString("notif_date") : "No date";

                                        notificationList.add(new UserNotificationClass(id, message, date));
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(UserNotificationActivity.this, "No notifications found.", R.style.accinputerror).show();
                                }
                            } else {
                                Toast.makeText(UserNotificationActivity.this, "Invalid server response.", R.style.accinputerror).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONParseError", "Error: " + e.getMessage());
                            Toast.makeText(UserNotificationActivity.this, "Error parsing data: " + e.getMessage(), R.style.accinputerror).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
                Toast.makeText(UserNotificationActivity.this, "Failed to fetch notifications", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}
