package com.example.ecommerceapp.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;

public class OrderListHolder extends RecyclerView.ViewHolder{
    TextView orderDate, orderPrice, orderId, orderStatus;

    public OrderListHolder(@NonNull View itemView) {
        super(itemView);

        orderDate = itemView.findViewById(R.id.orderDate);
        orderPrice = itemView.findViewById(R.id.orderPrice);
        orderId = itemView.findViewById(R.id.orderId);
        orderStatus = itemView.findViewById(R.id.orderStatus);
    }
}
