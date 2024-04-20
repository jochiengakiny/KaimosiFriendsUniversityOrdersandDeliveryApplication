package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

public class OrderModel {
    String orderId, userId, phone, meals, location, total, date, approved;

    public OrderModel() {
    }

    public OrderModel(String orderId, String userId, String phone, String meals, String location, String total, String date, String approved) {
        this.orderId = orderId;
        this.userId = userId;
        this.phone = phone;
        this.meals = meals;
        this.location = location;
        this.total = total;
        this.date = date;
        this.approved = approved;
    }

    public String getApproved() {
        return approved;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getMeals() {
        return meals;
    }

    public String getLocation() {
        return location;
    }

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }
}
