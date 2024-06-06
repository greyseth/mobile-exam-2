package com.example.ecommerceapp.holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.ItemDetails;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.CartManager;
import com.example.ecommerceapp.data.ImageDownloader;
import com.example.ecommerceapp.models.CartItem;
import com.example.ecommerceapp.models.Item;

import java.util.LinkedList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListHolder> {
    Context context;
    List<Item> items = new LinkedList<>();

    public ItemListAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemListHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListHolder holder, int position) {
        Item i = items.get(position);

        holder.itemName.setText(i.getName());
        holder.itemPrice.setText("Rp. "+String.format("%,d", i.getPrice()));
//        holder.itemPrice.setText(String.valueOf(i.getPrice()));
        if (!i.getImage().isEmpty()) new ImageDownloader(holder.itemImage).execute(API.apiUrl+"/images/"+i.getImage());

        if (CartManager.tempCart.stream().filter(ci -> ci.getItemId() == i.getItemId()).count() > 0) holder.hasAddded.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if user already has the item in their cart

                if (CartManager.tempCart.stream().filter(ci -> ci.getItemId() == i.getItemId()).count() > 0) {
                    Toast.makeText(context, "Barang ini sudah ada di keranjang anda!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(context, ItemDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("item_id", String.valueOf(i.getItemId()));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
