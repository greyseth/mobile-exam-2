package com.example.ecommerceapp.models;

public class Item {
    int itemId;
    String name;
    String description;
    int price;
    String image = "";
    int ordersCount;
    int quantity;

    public Item(int itemId, String name, String description, int price, int ordersCount) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ordersCount = ordersCount;
    }

    public Item(int itemId, String name, String description, int price, int ordersCount, String image) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ordersCount = ordersCount;
        this.image = image;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(int ordersCount) {
        this.ordersCount = ordersCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
