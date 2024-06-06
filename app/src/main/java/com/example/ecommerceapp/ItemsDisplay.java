package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.data.API;
import com.example.ecommerceapp.data.AccountManager;
import com.example.ecommerceapp.data.CartManager;
import com.example.ecommerceapp.holders.ItemListAdapter;
import com.example.ecommerceapp.interfaces.ApiResponse;
import com.example.ecommerceapp.models.CartItem;
import com.example.ecommerceapp.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ItemsDisplay extends AppCompatActivity {

    ProgressBar loading;
    RecyclerView itemsList;

    EditText searchInput;
    ImageButton searchButton;
    TextView notFound;

    List<Item> items = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_display);

        loading = findViewById(R.id.loading);
        itemsList = findViewById(R.id.itemsView);

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        notFound = findViewById(R.id.notFound);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchInput.getText().toString().isEmpty()) fetchData();
                else fetchSearchData();
            }
        });

        fetchCartData();
        fetchData();
    }

    public void fetchData() {
        notFound.setVisibility(View.GONE);
        itemsList.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        API.get(ItemsDisplay.this, "/items/objectified", new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        items.clear();

                        JSONArray data = response.get().getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject d = data.getJSONObject(i);
                            Item addItem;

                            if (d.isNull("image")) addItem = new Item(d.getInt("item_id"), d.getString("name"), d.getString("description"), d.getInt("price"), d.getInt("orders_count"));
                            else addItem = new Item(d.getInt("item_id"), d.getString("name"), d.getString("description"), d.getInt("price"), d.getInt("orders_count"), d.getString("image"));

                            items.add(addItem);
                        }

                        updateItemDisplay();

                        itemsList.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(ItemsDisplay.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        System.out.println(response);
                    }
                }else {
                    Toast.makeText(ItemsDisplay.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    public void fetchSearchData() {
        notFound.setVisibility(View.GONE);
        itemsList.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        API.get(ItemsDisplay.this, "/items/search/"+searchInput.getText().toString(), new ApiResponse() {
            @Override
            public void onResponse(Optional<JSONObject> response) throws JSONException {
                if (response.isPresent()) {
                    if (!response.get().has("error")) {
                        if (!response.get().has("empty")) {
                            items.clear();

                            JSONArray data = response.get().getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject d = data.getJSONObject(i);
                                Item addItem;

                                if (d.isNull("image")) addItem = new Item(d.getInt("item_id"), d.getString("name"), d.getString("description"), d.getInt("price"), d.getInt("orders_count"));
                                else addItem = new Item(d.getInt("item_id"), d.getString("name"), d.getString("description"), d.getInt("price"), d.getInt("orders_count"), d.getString("image"));

                                items.add(addItem);
                            }

                            updateItemDisplay();

                            itemsList.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }else {
                            notFound.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(ItemsDisplay.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        System.out.println(response);
                    }
                }else {
                    Toast.makeText(ItemsDisplay.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    public void fetchCartData() {
        API.get(ItemsDisplay.this, "/cart/" + AccountManager.loggedinId, new ApiResponse() {
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

                                updateItemDisplay();
                            }
                        }else {
                            CartManager.tempCart.clear();
                        }
                    }else {
                        Toast.makeText(ItemsDisplay.this, "Sebuah kesalahan terjadi!", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                }else {
                    Toast.makeText(ItemsDisplay.this, "Sebuah Kesalahan Terjadi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void updateItemDisplay() {
        itemsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemsList.setAdapter(new ItemListAdapter(getApplicationContext(), items));
    }
}