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
    public long timestamp; // czas powiadomienia/promocji (System.currentTimeMillis())
    public String message; // opcjonalnie treść powiadomienia/opisu

    public HistoryEntry(String productName, String shopName, double dealPrice, long timestamp, String message) {
        this.productName = productName;
        this.shopName = shopName;
        this.dealPrice = dealPrice;
        this.timestamp = timestamp;
        this.message = message;
    }
}