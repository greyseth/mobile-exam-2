package com.example.ecommerceapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ecommerceapp.Account;
import com.example.ecommerceapp.Cart;
import com.example.ecommerceapp.ItemsDisplay;
import com.example.ecommerceapp.R;

public class Navbar extends Fragment implements View.OnClickListener {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_navbar, container, false);

        LinearLayout buyBtn = view.findViewById(R.id.buyBtn);
        LinearLayout cartBtn = view.findViewById(R.id.cartBtn);
        LinearLayout accountBtn = view.findViewById(R.id.accountBtn);

        buyBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        accountBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buyBtn) {
            Intent i = new Intent(getContext(), ItemsDisplay.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else if (v.getId() == R.id.cartBtn) {
            Intent i = new Intent(getContext(), Cart.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else if (v.getId() == R.id.accountBtn) {
            Intent i = new Intent(getContext(), Account.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}