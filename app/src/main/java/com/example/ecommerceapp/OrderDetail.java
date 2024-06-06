package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.holders.OrderDetailAdapter;
import com.example.ecommerceapp.interfaces.ApiResponse;
import com.example.ecommerceapp.models.Item;
import com.example.ecommerceapp.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Optional;

public class OrderDetail extends AppCompatActivity {

    ImageButton backBtn;
    ProgressBar loading;

    LinearLayout orderDescription;
    RecyclerView itemsList;

    TextView orderIdText, orderDate, orderPayment, orderStatus;

    int orderId;
    Order orderData;
    LinkedList<Item> orderItems = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        backBtn = findViewById(R.id.backBtn);
        loading = findViewById(R.id.loading);
        orderDescription = findViewById(R.id.orderDescription);
        itemsList = findViewById(R.id.itemsList);

        orderIdText = findViewById(R.id.orderId);
        orderDate = findViewById(R.id.orderDate);
        orderPayment = findViewById(R.id.orderPayment);
        orderStatus = findViewById(R.id.orderStatus);

        Bundle bundle = getIntent().getExtras();
        if (!bundle.getString("order_id").isEmpty())
            orderId = Integer.parseInt(bundle.getString("order_id"));
        else startActivity(new Intent(OrderDetail.this, OrdersList.class));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetail.this, OrdersList.class));
            }
        });

        fetchData();
    }

    void enableList(boolean enabled) {
        if (enabled) {
            loading.setVisibility(View.GONE);
            orderDescription.setVisibility(View.VISIBLE);
            itemsList.setVisibility(View.VISIBLE);
        }else {
            loading.setVisibility(View.VISIBLE);
            orderDescription.setVisibility(View.GONE);
            itemsList.setVisibility(View.GONE);
        }
    }

    void updateItemsView() {
        itemsList.setLayoutManager(new LinearLayoutManager(OrderDetail.this));
        itemsList.setAdapter(new OrderDetailAdapter(OrderDetail.this, orderItems));

        orderIdText.setText("Id Pemesanan: "+orderId);
        orderDate.setText("Tanggal Pemesanan: "+orderData.getDateOrdered());
        orderPayment.setText("Total Pembayaran: Rp. "+String.format("%,d", orderData.getTotalPrice()));
        orderStatus.setText("Status Pemesanan: "+orderData.getStatus());
    }

    void fetchData() {
        API.get(OrderDetail.this, "/orders/" + orderId, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        JSONObject orderDetails = response.get().getJSONObject("data");
                        int userId = orderDetails.getInt("user_id");
                        int totalPrice = orderDetails.getInt("total_price");
                        String status = orderDetails.getString("status");
                        String dateOrdered = orderDetails.getString("date_ordered");
                        orderData = new Order(orderId, userId, totalPrice, status, dateOrdered);

                        API.get(OrderDetail.this, "/orders/detail/" + orderId, new ApiResponse() {
                            @Override
                            public void onResponse(Optional<JSONObject> response2) throws JSONException {
                                if (response2.isPresent()) {
                                    if (!response2.get().has("error")) {
                                        JSONArray data2 = response2.get().getJSONArray("data");
                                        for (int i = 0; i < data2.length(); i++) {
                                            JSONObject orderDetailData = data2.getJSONObject(i);

                                            int itemId = orderDetailData.getInt("item_id");
                                            String name = orderDetailData.getString("name");
                                            int price = orderDetailData.getInt("price");
                                            int quantity = orderDetailData.getInt("quantity");
                                            String image = orderDetailData.getString("image");

                                            Item itemAdd = new Item(itemId, name, "", price, quantity, image);
                                            orderItems.add(itemAdd);
                                        }

                                        enableList(true);
                                        updateItemsView();
                                    }else {
                                        Toast.makeText(OrderDetail.this, "Sebuah kesalahan terjadi", Toast.LENGTH_SHORT).show();
                                        System.out.println(response2);
                                    }
                                }else
                                    Toast.makeText(OrderDetail.this, "Sebuah kesahalan terjadi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(OrderDetail.this, "Sebuah kesalahan terjadi", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                }else
                    Toast.makeText(OrderDetail.this, "Sebuah kesalahan terjadi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}