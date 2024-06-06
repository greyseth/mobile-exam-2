package com.example.ecommerceapp.models;

public class CartItem {
    int itemId;
    int ownerId;
    String name;
    int price;
    String image = "";
    int quantity = 0;

    public CartItem(int itemId, int ownerId, String name, int price, String image, int quantity) {
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public CartItem(int itemId, int ownerId, String name, int price, int quantity) {
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
