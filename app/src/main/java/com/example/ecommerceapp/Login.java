package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.interfaces.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Login extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    CheckBox rememberMeInput;
    Button loginButton;

    LinearLayout loadingScreen;
    TextView signupRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loadingScreen = findViewById(R.id.loadingScreen);
        rememberMeInput = findViewById(R.id.rememberMeInput);
        loginButton = findViewById(R.id.loginButton);
        signupRedirect = findViewById(R.id.signupRedirect);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Semua field harus terisi!", Toast.LENGTH_SHORT).show();
                }

                performLogin(email, password);
            }
        });

        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
    }

    void performLogin(String email, String password) {
        loadingScreen.setVisibility(View.VISIBLE);

        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        }catch(JSONException e) {e.printStackTrace();}
        API.post(Login.this, "/users/login", params, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (response.get().has("error")) {
                        Toast.makeText(Login.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                        System.out.println(response.get());
                        loadingScreen.setVisibility(View.GONE);
                    }else {
                        if (response.get().getBoolean("success")) {
                            if (rememberMeInput.isChecked()) {
                                //Writes to SharedPreferences
                                SharedPreferences sp = Login.this.getSharedPreferences("RetroEmporiumPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("loginId", Integer.parseInt(response.get().getString("user_id")));
                                editor.apply();
                            }

                            AccountManager.loggedinId = Integer.parseInt(response.get().getString("user_id"));
                            startActivity(new Intent(Login.this, MainActivity.class));
                        }else {
                            Toast.makeText(Login.this, "Email/Password tidak sesuai!", Toast.LENGTH_SHORT).show();
                            loadingScreen.setVisibility(View.GONE);
                        }
                    }
                }else {
                    Toast.makeText(Login.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                    loadingScreen.setVisibility(View.GONE);
                }
            }
        });
    }
}