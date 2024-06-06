package com.example.ecommerceapp.holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.OrderDetail;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.Order;

import java.util.LinkedList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListHolder> {
    Context context;
    LinkedList<Order> orderList;

    public OrderListAdapter(Context context, LinkedList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListHolder holder, int position) {
        Order orderItem = orderList.get(position);

        holder.orderId.setText("Id: "+orderItem.getOrderId());
        holder.orderDate.setText(orderItem.getDateOrdered());
        holder.orderPrice.setText("Rp. "+orderItem.getTotalPrice());
        holder.orderStatus.setText(orderItem.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OrderDetail.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("order_id", String.valueOf(orderItem.getOrderId()));
                context.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public OrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderListHolder(LayoutInflater.from(context).inflate(R.layout.list_order, parent, false));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
