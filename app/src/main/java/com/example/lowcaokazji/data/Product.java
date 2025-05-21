package com.example.lowcaokazji.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wishlist")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String url;
    public String category;
    public String imageUri;
    public double targetPrice;
    public String description;

    public Product(String name, String url, String category, String imageUri, double targetPrice, String description) {
        this.name = name;
        this.url = url;
        this.category = category;
        this.imageUri = imageUri;
        this.targetPrice = targetPrice;
        this.description = description;
    }
}