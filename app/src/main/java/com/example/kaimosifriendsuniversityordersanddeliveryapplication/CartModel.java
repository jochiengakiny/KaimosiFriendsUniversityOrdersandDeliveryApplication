package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

public class CartModel {
    String mealId,quantity,date,userId;
    boolean ordered;

    public CartModel() {
    }

    public CartModel(String mealId, String quantity, String date, String userId) {
        this.mealId = mealId;
        this.quantity = quantity;
        this.date = date;
        this.userId = userId;
        this.ordered = false;
    }

    public CartModel(String mealId, String quantity, String date, String userId, boolean ordered) {
        this.mealId = mealId;
        this.quantity = quantity;
        this.date = date;
        this.userId = userId;
        this.ordered = ordered;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public String getMealId() {
        return mealId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }
}
