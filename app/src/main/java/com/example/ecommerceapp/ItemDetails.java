package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.data.CartManager;
import com.example.ecommerceapp.data.ImageDownloader;
import com.example.ecommerceapp.interfaces.ApiResponse;
import com.example.ecommerceapp.models.CartItem;
import com.example.ecommerceapp.models.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class ItemDetails extends AppCompatActivity {

    LinearLayout backBtn;

    ProgressBar loading;
    ScrollView contentContainer;
    ImageView itemImage;
    TextView itemNameDisplay;
    TextView itemPriceDisplay;
    TextView itemDescriptionDisplay;
    TextView itemOrdersDisplay;

    Button addBtn;

    LinearLayout orderedNotif;
    ImageView orderedNotifClose;

    int itemId;
    Item itemData;

    View.OnClickListener addBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            contentContainer.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

            JSONObject params = new JSONObject();
            try {
                params.put("user_id", AccountManager.loggedinId);
                params.put("item_id", itemId);
            }catch (JSONException e) {e.printStackTrace();}
            API.post(ItemDetails.this, "/cart/order", params, new ApiResponse() {
                @Override
                public void onResponse(Optional<JSONObject> response) throws JSONException {
                    if (response.isPresent()) {
                        if (!response.get().has("error")) {
                            CartItem tempCartAdd = new CartItem(itemData.getItemId(), AccountManager.loggedinId, itemData.getName(), itemData.getPrice(), itemData.getImage(), 1);
                            CartManager.tempCart.add(tempCartAdd);

                            contentContainer.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);

                            orderedNotif.setVisibility(View.VISIBLE);

                            addBtn.setText("Lihat Keranjang");
                            addBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(ItemDetails.this, Cart.class));
                                }
                            });
                        }else {
                            Toast.makeText(ItemDetails.this, "Sebuah kesalahan telah terjadi", Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                        }
                    }else
                        Toast.makeText(ItemDetails.this, "Sebuah kesalahan telah terjadi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        backBtn = findViewById(R.id.backBtn);
        loading = findViewById(R.id.loading);
        contentContainer = findViewById(R.id.contentContainer);
        itemImage = findViewById(R.id.itemImage);
        itemNameDisplay = findViewById(R.id.itemNameDisplay);
        itemPriceDisplay = findViewById(R.id.itemPriceDisplay);
        itemDescriptionDisplay = findViewById(R.id.itemDescriptionDisplay);
        itemOrdersDisplay = findViewById(R.id.itemOrdersDisplay);
        addBtn = findViewById(R.id.addBtn);

        orderedNotif = findViewById(R.id.orderedNotif);
        orderedNotifClose = findViewById(R.id.orderedNotifClose);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getString("item_id") != null) {
                itemId = Integer.parseInt(bundle.getString("item_id"));
            }
        }

        fetchData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemDetails.this, ItemsDisplay.class));
            }
        });

        addBtn.setOnClickListener(addBtnOnClick);

        orderedNotifClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderedNotif.setVisibility(View.GONE);

                addBtn.setText("Tambah Ke Keranjang");
                addBtn.setOnClickListener(addBtnOnClick);
            }
        });
    }

    public void fetchData() {
        contentContainer.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        API.get(ItemDetails.this, "/items/" + itemId, new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        String name = response.get().getString("name");
                        String description = response.get().getString("description");
                        int price = response.get().getInt("price");
                        String image = response.get().isNull("image")?"":response.get().getString("image");
                        int ordersCount = response.get().getInt("orders_count");

                        if(image.isEmpty()) itemData = new Item(itemId, name, description, price, ordersCount);
                        else itemData = new Item(itemId, name, description, price, ordersCount, image);

                        setDisplay();
                        loading.setVisibility(View.GONE);
                        contentContainer.setVisibility(View.VISIBLE);
                        addBtn.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(ItemDetails.this, "Sebuah kesalahan telah terjadi!", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                }else Toast.makeText(ItemDetails.this, "Sebuah kesalahan telah terjadi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDisplay() {
        itemNameDisplay.setText(itemData.getName());
        itemDescriptionDisplay.setText(itemData.getDescription());
        itemPriceDisplay.setText("Rp. "+String.format("%,d", itemData.getPrice()));
        if (!itemData.getImage().isEmpty()) new ImageDownloader(itemImage).execute(API.apiUrl+"/images/"+itemData.getImage());
        if (itemData.getOrdersCount() > 0) itemOrdersDisplay.setText(itemData.getOrdersCount()+" Dari Produk ini telah terjual");
        else itemOrdersDisplay.setText("Jadilah Yang Pertama Untuk Membeli Produk Ini!");
    }
}