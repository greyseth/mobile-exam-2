package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.interfaces.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Account extends AppCompatActivity {

    ProgressBar loading;
    ScrollView contentContainer;
    TextView idDisplay;
    TextView usernameDisplay;
    TextView emailDisplay;
    TextView passwordDisplay;

    Button ordersListButton;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        loading = findViewById(R.id.loading);
        contentContainer = findViewById(R.id.contentContainer);
        idDisplay = findViewById(R.id.idDisplay);
        usernameDisplay = findViewById(R.id.usernameDisplay);
        emailDisplay = findViewById(R.id.emailDisplay);
        passwordDisplay = findViewById(R.id.passwordDisplay);
        logoutButton = findViewById(R.id.logoutButton);
        ordersListButton = findViewById(R.id.ordersListButton);

        fetchData();

        ordersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, OrdersList.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove SharedPreferences Data
                SharedPreferences sp = Account.this.getSharedPreferences("RetroEmporiumPrefs", MODE_PRIVATE);
                SharedPreferences.Editor spEdit = sp.edit();
                spEdit.clear();
                spEdit.apply();

                AccountManager.loggedinId = -1;
                startActivity(new Intent(Account.this, MainActivity.class));
            }
        });
    }

    public void fetchData() {
        loading.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        logoutButton.setVisibility(View.GONE);

        API.get(Account.this, "/users/objectified/" + AccountManager.loggedinId, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        JSONObject data = response.get().getJSONObject("data");
                        idDisplay.setText(String.valueOf(AccountManager.loggedinId));
                        usernameDisplay.setText(data.getString("username"));
                        emailDisplay.setText(data.getString("email"));
                        passwordDisplay.setText(data.getString("password"));

                        contentContainer.setVisibility(View.VISIBLE);
                        logoutButton.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(Account.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        System.out.println(response);
                    }
                }else {
                    Toast.makeText(Account.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}