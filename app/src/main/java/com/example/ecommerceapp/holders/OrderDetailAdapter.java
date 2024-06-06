package com.example.ecommerceapp.holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.ImageDownloader;
import com.example.ecommerceapp.models.Item;

import java.util.LinkedList;

public class OrderDetailAdapter extends RecyclerView.Adapter {
    Context context;
    LinkedList<Item> items;

    public OrderDetailAdapter(Context context, LinkedList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDetailHolder(LayoutInflater.from(context).inflate(R.layout.list_order_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);

        TextView itemName = holder.itemView.findViewById(R.id.itemName);
        TextView itemPrice = holder.itemView.findViewById(R.id.itemPrice);
        TextView itemQuantity = holder.itemView.findViewById(R.id.itemQuantity);

        new ImageDownloader(holder.itemView.findViewById(R.id.imageView)).execute(API.apiUrl+"/images/"+item.getImage());
        itemName.setText(item.getName());
        itemPrice.setText("Rp. "+String.format("%,d", item.getPrice()));
        itemQuantity.setText("Qty: "+item.getOrdersCount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
