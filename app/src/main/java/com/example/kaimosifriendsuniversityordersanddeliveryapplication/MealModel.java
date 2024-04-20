package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

public class MealModel {
    String id, name, price, category,description, image, date;

    public MealModel() {
    }

    public MealModel(String id, String name, String price, String category, String description, String image, String date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }
}
