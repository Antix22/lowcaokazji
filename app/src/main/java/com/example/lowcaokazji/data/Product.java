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
    public double targetPrice;

    public Product(String name, String url, String category, double targetPrice) {
        this.name = name;
        this.url = url;
        this.category = category;
        this.targetPrice = targetPrice;
    }
}