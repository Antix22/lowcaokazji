package com.example.lowcaokazji.model;

import java.util.HashMap;
import java.util.Map;

public class PriceInfo {
    private String productName;
    private Map<String, Double> shopPrices; // nazwa sklepu -> cena

    public PriceInfo(String productName) {
        this.productName = productName;
        this.shopPrices = new HashMap<>();
    }

    public String getProductName() {
        return productName;
    }

    public Map<String, Double> getShopPrices() {
        return shopPrices;
    }

    public void addShopPrice(String shopName, double price) {
        shopPrices.put(shopName, price);
    }

    public Double getPriceForShop(String shopName) {
        return shopPrices.get(shopName);
    }
}