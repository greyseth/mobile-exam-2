package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.holders.OrderListAdapter;
import com.example.ecommerceapp.interfaces.ApiResponse;
import com.example.ecommerceapp.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Optional;

public class OrdersList extends AppCompatActivity {

    ImageButton backBtn;
    ProgressBar loading;
    RecyclerView ordersList;

    LinearLayout noOrders;
    Button startShopping;

    LinkedList<Order> userOrders = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        if (AccountManager.loggedinId < 0) {
            startActivity(new Intent(OrdersList.this, Login.class));
            return;
        }

        backBtn = findViewById(R.id.backBtn);
        loading = findViewById(R.id.loading);
        ordersList = findViewById(R.id.ordersList);

        noOrders = findViewById(R.id.noOrders);
        startShopping = findViewById(R.id.startShopping);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrdersList.this, Account.class));
            }
        });

        fetchData();
    }

    void fetchData() {
        loading.setVisibility(View.VISIBLE);
        ordersList.setVisibility(View.GONE);

        API.get(OrdersList.this, "/orders/from/"+ AccountManager.loggedinId, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        if (!response.get().has("empty")) {
                            JSONArray data = response.get().getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject order = data.getJSONObject(i);

                                int orderId = order.getInt("order_id");
                                int userId = order.getInt("user_id");
                                int totalPrice = order.getInt("total_price");
                                String status = order.getString("status");
                                String orderedDate = order.getString("date_ordered");

                                Order addOrder = new Order(orderId, userId, totalPrice, status, orderedDate);
                                userOrders.add(addOrder);

                                showOrderList();
                            }
                        }else {
                            loading.setVisibility(View.GONE);
                            noOrders.setVisibility(View.VISIBLE);
                            startShopping.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(OrdersList.this, ItemsDisplay.class));
                                }
                            });
                        }
                    }else {
                        Toast.makeText(OrdersList.this, "Sebuah Kesalahan Terjadi", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                }else
                    Toast.makeText(OrdersList.this, "Sebuah Kesalahan Terjadi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showOrderList() {
        loading.setVisibility(View.GONE);
        ordersList.setVisibility(View.VISIBLE);

        ordersList.setLayoutManager(new LinearLayoutManager(OrdersList.this));
        ordersList.setAdapter(new OrderListAdapter(OrdersList.this, userOrders));
    }
}