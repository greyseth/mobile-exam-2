package com.example.ecommerceapp.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;

public class ItemListHolder extends RecyclerView.ViewHolder {
    ImageView itemImage;
    TextView itemName;
    TextView itemPrice;
    ImageView hasAddded;

    public ItemListHolder(@NonNull View itemView) {
        super(itemView);

        itemImage = itemView.findViewById(R.id.itemImage);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        hasAddded = itemView.findViewById(R.id.hasAdded);
    }
}
