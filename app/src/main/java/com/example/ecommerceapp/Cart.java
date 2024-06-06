package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.data.CartManager;
import com.example.ecommerceapp.holders.CartListAdapter;
import com.example.ecommerceapp.interfaces.ApiResponse;
import com.example.ecommerceapp.interfaces.CartUpdate;
import com.example.ecommerceapp.models.CartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Optional;

public class Cart extends AppCompatActivity implements CartUpdate {

    ProgressBar loading;
    TextView nocart;
    RecyclerView cartItemDisplay;
    TextView totalPrice;
    Button orderButton;

    LinearLayout orderLoadingContainer;
    LinearLayout orderedNotif;
    Button viewOrderButton;
    ImageView orderedNotifClose;

    int ordersTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        loading = findViewById(R.id.loading);
        nocart = findViewById(R.id.nocart);
        cartItemDisplay = findViewById(R.id.cartItemDisplay);
        totalPrice = findViewById(R.id.totalPrice);
        orderButton = findViewById(R.id.orderButton);

        orderLoadingContainer = findViewById(R.id.orderLoadingContainer);
        orderedNotif = findViewById(R.id.orderedNotif);
        viewOrderButton = findViewById(R.id.viewOrderButton);
        orderedNotifClose = findViewById(R.id.orderedNotifClose);

        fetchData();

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderLoadingContainer.setVisibility(View.VISIBLE);

                JSONObject params = new JSONObject();
                try {
                    params.put("user_id", AccountManager.loggedinId);
                    params.put("total_price", ordersTotalPrice);
                }catch (JSONException e) {e.printStackTrace();}
                API.post(Cart.this, "/orders/place", params, new ApiResponse() {
                    @Override
                    public void onResponse(Optional<JSONObject> response) throws JSONException {
                        if (!response.isPresent() || response.get().has("error")) {
                            Toast.makeText(Cart.this, "Sebuah kesalahan terjadi", Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                        }else {
                            orderedNotif.setVisibility(View.VISIBLE);
                            viewOrderButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Cart.this, OrderDetail.class);
                                    try {
                                        i.putExtra("order_id", response.get().getString("order_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            });

                            orderButton.setVisibility(View.GONE);
                            CartManager.tempCart.clear();
                        }
                    }
                });
            }
        });

        orderedNotifClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderButton.setVisibility(View.VISIBLE);
                orderedNotif.setVisibility(View.GONE);
            }
        });
    }

    public void fetchData() {
        setViews(true);

        API.get(Cart.this, "/cart/" + AccountManager.loggedinId, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        if (!response.get().has("empty")) {
                            CartManager.tempCart.clear();

                            JSONArray data = response.get().getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject cartItem = data.getJSONObject(i);
                                    int id = cartItem.getInt("item_id");
                                    String name = cartItem.getString("name");
                                    int price = cartItem.getInt("price");
                                    String image = "";
                                    if (!cartItem.isNull("image")) image = cartItem.getString("image");
                                    int quantity = cartItem.getInt("quantity");

                                    CartManager.tempCart.add(new CartItem(id, AccountManager.loggedinId, name, price, image, quantity));
                                }

                                updateCartDisplay();
                            }else {
                                setViews(false);
                                nocart.setVisibility(View.VISIBLE);
                            }
                        }else {
                            CartManager.tempCart.clear();
                            updateCartDisplay();
                        }
                    }else {
                        Toast.makeText(Cart.this, "Sebuah kesalahan terjadi!", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                        setViews(false);
                    }
                }else {
                    Toast.makeText(Cart.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                    setViews(false);
                }
            }
        });
    }

    @Override
    public void updateCartDisplay() {
        loading.setVisibility(View.GONE);
        cartItemDisplay.setVisibility(View.VISIBLE);

        LinkedList<CartItem> cartItems = CartManager.tempCart;
        if (CartManager.tempCart.size() > 0) {
            cartItemDisplay.setAdapter(new CartListAdapter(getApplicationContext(), cartItems, this));
            cartItemDisplay.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            orderButton.setVisibility(View.VISIBLE);
            totalPrice.setVisibility(View.VISIBLE);

            ordersTotalPrice = 0;
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem ci = cartItems.get(i);
                ordersTotalPrice += ci.getPrice() * ci.getQuantity();
            }
            totalPrice.setText("Total Harga: Rp. "+String.format("%,d", ordersTotalPrice));
        }else {
            nocart.setVisibility(View.VISIBLE);
            orderButton.setVisibility(View.GONE);
            totalPrice.setVisibility(View.GONE);
        }
    }

    public void setViews(boolean isLoading) {
        loading.setVisibility(isLoading?View.VISIBLE:View.GONE);
        if (isLoading) nocart.setVisibility(View.GONE);
        cartItemDisplay.setVisibility(isLoading?View.GONE:View.VISIBLE);
        orderButton.setVisibility(isLoading?View.GONE:View.VISIBLE);
        totalPrice.setVisibility(isLoading?View.GONE:View.VISIBLE);
    }
}