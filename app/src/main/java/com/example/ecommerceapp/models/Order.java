package com.example.ecommerceapp.models;

import java.util.Date;

public class Order {
    int orderId;
    int userId;
    int totalPrice;
    String status;
    String dateOrdered;

    public Order(int orderId, int userId, int totalPrice, String status, String dateOrdered) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.dateOrdered = dateOrdered;
    }

    public Order(int orderId, int userId, int totalPrice, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.dateOrdered = new Date().toString();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateOrdered() {
        return dateOrdered.substring(0, 10);
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }
}
