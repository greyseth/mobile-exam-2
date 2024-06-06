package com.example.ecommerceapp.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;

public class OrderDetailHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView itemName, itemPrice, itemQuantity;

    public OrderDetailHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        itemQuantity = itemView.findViewById(R.id.itemQuantity);
    }
}
