package com.example.lowcaokazji.model;

public class Coupon {
    private String code;
    private String description;
    private double discountValue; // np. 50.0 oznacza 50 zł zniżki
    private String shopName;

    public Coupon(String code, String description, double discountValue, String shopName) {
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.shopName = shopName;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public String getShopName() {
        return shopName;
    }
}