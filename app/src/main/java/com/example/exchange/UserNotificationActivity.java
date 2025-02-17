package com.example.exchange;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
        private static final String API_URL = "http://10.0.2.2/Exchange/fetch_notifications.php"; // Replace with your actual PHP API URL

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_notifications); // Make sure this XML file exists

            recyclerView = findViewById(R.id.orderupdatesrv); // Ensure ID matches your XML file
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            notificationList = new ArrayList<>();
            adapter = new UserNotificationAdapter(notificationList);
            recyclerView.setAdapter(adapter);

            fetchNotifications();
        }

        private void fetchNotifications() {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            notificationList.clear();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    int id = obj.getInt("notification_id");
                                    String message = obj.getString("message");
                                    String date = obj.getString("notif_date");

                                    notificationList.add(new UserNotificationClass(id, message, date));
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(UserNotificationActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
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

