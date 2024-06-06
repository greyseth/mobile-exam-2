package com.example.ecommerceapp.holders;

import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Cart;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.data.CartManager;
import com.example.ecommerceapp.data.ImageDownloader;
import com.example.ecommerceapp.interfaces.ApiResponse;
import com.example.ecommerceapp.interfaces.CartUpdate;
import com.example.ecommerceapp.models.CartItem;
import com.example.ecommerceapp.models.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CartListAdapter extends RecyclerView.Adapter<CartListHolder> {
    Context context;
    List<CartItem> cartItems = new LinkedList<>();
    CartUpdate cartUpdate;

    public CartListAdapter(Context context, List<CartItem> cartItems, CartUpdate cartUpdate) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartUpdate = cartUpdate;
    }

    @NonNull
    @Override
    public CartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartListHolder(LayoutInflater.from(context).inflate(R.layout.list_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartListHolder holder, int position) {
        int pos = position;
        CartItem i = cartItems.get(pos);

        holder.itemName.setText(i.getName());
        holder.itemPrice.setText("Rp. "+String.format("%,d", i.getPrice()));

        if (!i.getImage().isEmpty()) new ImageDownloader(holder.itemImage).execute(API.apiUrl+"/images/"+i.getImage());

        holder.itemQuantity.setText(String.valueOf(i.getQuantity()));

        holder.itemQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem previousCI = i;
                int previousQty = i.getQuantity();
                if (previousQty > 1) {
                    previousCI.setQuantity(previousQty-1);

                    CartManager.tempCart.set(pos, previousCI);

                    JSONObject params = new JSONObject();
                    try {
                        params.put("user_id", AccountManager.loggedinId);
                        params.put("item_id", i.getItemId());
                        params.put("quantity", previousQty-1);
                    }catch (JSONException e) {e.printStackTrace();}
                    API.post(context, "/cart/updateorder", params, new ApiResponse() {
                        @Override
                        public void onResponse(Optional<JSONObject> response) throws JSONException {
                            if (!response.isPresent() || response.get().has("error")) {
                                Toast.makeText(context, "Gagal mengubah kuantitas", Toast.LENGTH_SHORT).show();
                                System.out.println(response);
                            }
                        }
                    });
                }else {
                    CartManager.tempCart.remove(i);

                    JSONObject params = new JSONObject();
                    try {
                        params.put("user_id", AccountManager.loggedinId);
                        params.put("item_id", i.getItemId());
                    }catch (JSONException e) {e.printStackTrace();}
                    API.post(context, "/cart/removeorder", params, new ApiResponse() {
                        @Override
                        public void onResponse(Optional<JSONObject> response) throws JSONException {
                            //I can explain...
                            if (response.isPresent()) {
                                if (response.get().has("error")) {
                                    Toast.makeText(context, "Sebuah kesahalan terjadi", Toast.LENGTH_SHORT).show();
                                    System.out.println(response);
                                }
                            }else Toast.makeText(context, "Sebuah kesalahan terjadi...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cartUpdate.updateCartDisplay();
            }
        });

        holder.itemQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem previousCI = i;
                int previousQty = i.getQuantity();
                previousCI.setQuantity(previousQty+1);
                CartManager.tempCart.set(pos, previousCI);

                JSONObject params = new JSONObject();
                try {
                    params.put("user_id", AccountManager.loggedinId);
                    params.put("item_id", i.getItemId());
                    params.put("quantity", previousCI.getQuantity());
                }catch (JSONException e) {e.printStackTrace();}
                API.post(context, "/cart/updateorder", params, new ApiResponse() {
                    @Override
                    public void onResponse(Optional<JSONObject> response) throws JSONException {
                        if (!response.isPresent() || response.get().has("error")) {
                            Toast.makeText(context, "Gagal mengubah kuantitias", Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                        }
                    }
                });

                cartUpdate.updateCartDisplay();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
