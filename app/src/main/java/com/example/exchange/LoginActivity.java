package com.example.exchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {
    EditText usernameinput, passwordinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize UI elements
        usernameinput = findViewById(R.id.usernameinput);
        passwordinput = findViewById(R.id.passwordinput);

        // Handle login button click
        findViewById(R.id.loginbtn).setOnClickListener(view -> {
            String username = usernameinput.getText().toString().trim();
            String password = passwordinput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                StyleableToast.makeText(LoginActivity.this, "Please enter username and password", R.style.accinputerror).show();
            } else {
                new LoginTask().execute(username, password);
            }
        });

        // Handle create account button click
        findViewById(R.id.createaccbtn).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    // AsyncTask for handling login request
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String apiUrl = "http://10.0.2.2/Exchange/login.php"; // Update with actual API URL

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Send POST data using username
                String postData = "username=" + username + "&password=" + password;
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                // Read response
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                return response.toString();
            } catch (Exception e) {
                Log.e("LoginTask", "Error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                StyleableToast.makeText(LoginActivity.this, "Network Error", R.style.accinputerror).show();
                return;
            }
            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");

                if (status.equals("success")) {
                    JSONObject user = response.getJSONObject("user");
                    int userId = user.getInt("user_id");
                    String firstName = user.getString("first_name");
                    String lastName = user.getString("last_name");

                    // Save user details in SharedPreferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("USER_ID", userId);
                    editor.putString("USER_FIRST_NAME", firstName);
                    editor.putString("USER_LAST_NAME", lastName);
                    editor.apply();

                    // Show welcome message
                    StyleableToast.makeText(LoginActivity.this, "Welcome, " + firstName + "!", R.style.placedordertoast).show();

                    // Navigate to user homepage
                    Intent intent = new Intent(LoginActivity.this, StaffProfileActivity.class);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                } else {
                    StyleableToast.makeText(LoginActivity.this, "Invalid username or password", R.style.accinputerror).show();
                }
            } catch (Exception e) {
                Log.e("LoginTask", "JSON Error: " + e.getMessage());
                StyleableToast.makeText(LoginActivity.this, "Login failed", R.style.accinputerror).show();
            }
        }
    }
}
