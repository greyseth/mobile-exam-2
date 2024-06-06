package com.example.ecommerceapp.holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;

public class CartListHolder extends RecyclerView.ViewHolder {
    ImageView itemImage,itemQuantityMinus, itemQuantityPlus;
    TextView itemName, itemPrice, itemQuantity;

    public CartListHolder(@NonNull View itemView) {
        super(itemView);

        itemImage = itemView.findViewById(R.id.itemImage);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        itemQuantity = itemView.findViewById(R.id.itemQuantity);
        itemQuantityMinus = itemView.findViewById(R.id.itemQuantityMinus);
        itemQuantityPlus = itemView.findViewById(R.id.itemQuantityPlus);
    }
}
