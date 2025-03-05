package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etFirstName, etLastName, etUserID, etEmail, etPassword;
    String userRole; // Default role

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.createaccount_page);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUserID = findViewById(R.id.etUserID);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.signupbtn).setOnClickListener(view -> {
            String FirstName = etFirstName.getText().toString().trim();
            String LastName = etLastName.getText().toString().trim();
            String Email = etEmail.getText().toString().trim();
            String UserID = etUserID.getText().toString().trim();
            String Password = etPassword.getText().toString().trim();

            FirstName = capitalizeFirstLetter(FirstName);
            LastName = capitalizeFirstLetter(LastName);

            String username = Email.contains("@") ? Email.split("@")[0].toLowerCase() : "";

            if (FirstName.isEmpty() || LastName.isEmpty() || Email.isEmpty() || UserID.isEmpty() || Password.isEmpty()) {
                StyleableToast.makeText(CreateAccountActivity.this, "Please fill in all fields.", R.style.accinputerror).show();
                return;
            }

            if (UserID.length() < 2) {
                StyleableToast.makeText(CreateAccountActivity.this, "Invalid Student ID", R.style.accinputerror).show();
                return;
            }

            String firstTwoDigits = UserID.substring(0, 2);
            if (firstTwoDigits.equals("20")) {
                userRole = "Student";
            } else if (firstTwoDigits.equals("21")) {
                userRole = "Developer";
            } else {
                StyleableToast.makeText(CreateAccountActivity.this, "Invalid Student ID", R.style.accinputerror).show();
                return;
            }

            if (!isValidEmail(Email, userRole)) {
                StyleableToast.makeText(CreateAccountActivity.this, "Invalid email! Must be \n@students.nu-dasma.edu.ph or \n@admin.nu-dasma.edu.ph", R.style.accinputerror).show();
                return;
            }

            sendDataToServer(FirstName, LastName, Email, UserID, username, Password, userRole);
        });

        findViewById(R.id.loginbtn).setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void sendDataToServer(String firstName, String lastName, String email, String userID, String username, String password, String userRole) {
        String url = "http://10.0.2.2/Exchange/signup.php";

        RequestBody body = new FormBody.Builder()
                .add("user_id", userID)
                .add("first_name", firstName)
                .add("last_name", lastName)
                .add("username", username)
                .add("email", email)
                .add("password", password)
                .add("user_role", userRole)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                final String responseData = response.body().string();

                runOnUiThread(() -> {
                    if (response.isSuccessful() && responseData.contains("success")) {
                        StyleableToast.makeText(CreateAccountActivity.this, "Sign up Successful", R.style.placedordertoast).show();
                        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        StyleableToast.makeText(CreateAccountActivity.this, "Signup Failed", R.style.accinputerror).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        StyleableToast.makeText(CreateAccountActivity.this, "Network Error", R.style.accinputerror).show()
                );
            }
        }).start();
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private boolean isValidEmail(String email, String userRole) {
        if (userRole.equals("Student")) {
            return email.toLowerCase().endsWith("@students.nu-dasma.edu.ph");
        }
        if (userRole.equals("Developer")){
            return email.toLowerCase().endsWith("@admin.nu-dasma.edu.ph");
        }
        return true;
    }
}
