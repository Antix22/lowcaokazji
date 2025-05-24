package com.example.lowcaokazji.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String productName;
    public String shopName;
    public double dealPrice;
    public long timestamp;
    public String message;
    public String username; // <- dodaj to pole!

    public HistoryEntry(String productName, String shopName, double dealPrice, long timestamp, String message, String username) {
        this.productName = productName;
        this.shopName = shopName;
        this.dealPrice = dealPrice;
        this.timestamp = timestamp;
        this.message = message;
        this.username = username;
    }
}