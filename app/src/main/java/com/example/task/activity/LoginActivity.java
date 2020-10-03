package com.example.task.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.task.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.task.activity.SplashScreenActivity.ID;
import static com.example.task.activity.SplashScreenActivity.SHARED_PREFS;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                boolean isEmpty = false;

                if (email.isEmpty()) {
                    isEmpty = true;
                    edtEmail.setError("Email harus diisi");
                }

                if (password.isEmpty()) {
                    isEmpty = true;
                    edtPassword.setError("Password harus diisi");
                }

                if (!isEmpty) {
                    AndroidNetworking.post("http://192.168.43.21/bike_rental/login.php")
                            .addBodyParameter("email", email)
                            .addBodyParameter("password", password)
                            .addBodyParameter("role", String.valueOf(2))
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");
                                        String message = response.getString("message");

                                        if (status.equals("success")) {
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                                            String id = response.getString("id");

                                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString(ID, id);
                                            editor.apply();

                                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onError: " + error.getLocalizedMessage());
                                }
                            });
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}