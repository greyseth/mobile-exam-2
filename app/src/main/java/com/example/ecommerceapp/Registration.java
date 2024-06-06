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

public class Registration extends AppCompatActivity {

    EditText usernameInput;
    EditText emailInput;
    EditText passwordInput;
    EditText confirmPasswordInput;
    Button registerButton;
    CheckBox rememberMeInput;
    TextView loginRedirect;

    LinearLayout loadingScreen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        loadingScreen = findViewById(R.id.loadingScreen);
        rememberMeInput = findViewById(R.id.rememberMeInput);
        loginRedirect = findViewById(R.id.loginRedirect);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                if (username.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty()) {
                    Toast.makeText(Registration.this, "Semua field harus terisi!", Toast.LENGTH_SHORT).show();
                }

                if (!password.equals(confirmPassword))
                    Toast.makeText(Registration.this, "Password tidak sesuai!", Toast.LENGTH_SHORT).show();

                performSignup(username, email, password);
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }

    void performSignup(String username, String email, String password) {
        loadingScreen.setVisibility(View.VISIBLE);

        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
        }catch(JSONException e) {e.printStackTrace();}
        API.post(Registration.this, "/users/signup", params, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (response.get().has("error")) {
                        Toast.makeText(Registration.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                        loadingScreen.setVisibility(View.GONE);
                    }else {
                        if (rememberMeInput.isChecked()) {
                            //Writes to SharedPreferences
                            SharedPreferences sp = Registration.this.getSharedPreferences("RetroEmporiumPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("loginId", Integer.parseInt(response.get().getString("user_id")));
                            editor.apply();
                        }

                        AccountManager.loggedinId = Integer.parseInt(response.get().getString("user_id"));
                        startActivity(new Intent(Registration.this, MainActivity.class));
                    }
                }else {
                    Toast.makeText(Registration.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                    loadingScreen.setVisibility(View.GONE);
                }
            }
        });
    }
}