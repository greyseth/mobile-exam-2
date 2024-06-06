package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerceapp.data.AccountManager;

public class MainActivity extends AppCompatActivity {

    //Main Loading Page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = MainActivity.this.getSharedPreferences("RetroEmporiumPrefs", MODE_PRIVATE);
        if (AccountManager.loggedinId == -1) AccountManager.loggedinId = sp.getInt("loginId", -1);
//        int myheadhurts = sp.getInt(getString(R.string.sharedprefskey)+"_loginid", -1);
//        Toast.makeText(this, String.valueOf(myheadhurts), Toast.LENGTH_SHORT).show();

        if (AccountManager.loggedinId == -1) startActivity(new Intent(MainActivity.this, Login.class));
        else startActivity(new Intent(MainActivity.this, ItemsDisplay.class));

//        try {
//            Thread.sleep(2500);
//
//            if (AccountManager.loggedinId == -1) startActivity(new Intent(MainActivity.this, Login.class));
//            else Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}