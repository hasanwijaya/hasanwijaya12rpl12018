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

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText edtName, edtNoktp, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmationPassword;
    private Button btnRegister;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edt_name);
        edtNoktp = findViewById(R.id.edt_noktp);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmationPassword = findViewById(R.id.edt_confirmation_password);
        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName= edtName.getText().toString().trim();
                String inputNoktp= edtNoktp.getText().toString().trim();
                String inputEmail = edtEmail.getText().toString().trim();
                String inputPhone = edtPhone.getText().toString().trim();
                String inputAddress = edtAddress.getText().toString().trim();
                String inputPassword = edtPassword.getText().toString().trim();
                String inputConfirmationPassword = edtConfirmationPassword.getText().toString().trim();

                boolean isEmpty = false;
                boolean isInvalidPassword = false;

                if (inputName.isEmpty()) {
                    isEmpty = true;
                    edtName.setError("Nama lengkap harus diisi");
                }

                if (inputNoktp.isEmpty()) {
                    isEmpty = true;
                    edtNoktp.setError("No KTP harus diisi");
                }

                if (inputEmail.isEmpty()) {
                    isEmpty = true;
                    edtEmail.setError("Email harus diisi");
                }

                if (inputPhone.isEmpty()) {
                    isEmpty = true;
                    edtPhone.setError("Nomor telepon harus diisi");
                }

                if (inputAddress.isEmpty()) {
                    isEmpty = true;
                    edtAddress.setError("Alamat Number harus diisi");
                }

                if (inputPassword.isEmpty()) {
                    isEmpty = true;
                    edtPassword.setError("Password harus diisi");
                }

                if (inputConfirmationPassword.isEmpty()) {
                    isEmpty = true;
                    edtConfirmationPassword.setError("Confirm Password harus diisi");
                }

                if (!inputPassword.equals(inputConfirmationPassword)) {
                    isInvalidPassword = true;
                    edtConfirmationPassword.setError("Password & Confirm Password don't match");
                }

                if (!isEmpty && !isInvalidPassword) {
                    AndroidNetworking.post("http://192.168.43.21/rental_bike/register.php")
                            .addBodyParameter("name", inputName)
                            .addBodyParameter("noktp", inputNoktp)
                            .addBodyParameter("email", inputEmail)
                            .addBodyParameter("phone", inputPhone)
                            .addBodyParameter("address", inputAddress)
                            .addBodyParameter("password", inputPassword)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");
                                        String message = response.getString("message");

                                        if (status.equals("success")) {
                                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString(ID, "1");
                                            editor.apply();

                                            Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    Log.e(TAG, "onError: " + error.getLocalizedMessage());
                                }
                            });
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}