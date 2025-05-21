package com.example.lowcaokazji.model;

public class Review {
    private String productName;
    private String content;
    private boolean isPositive; // Możesz analizować sentyment (true/false) lub zawsze true jeśli tylko przykładowe

    public Review(String productName, String content, boolean isPositive) {
        this.productName = productName;
        this.content = content;
        this.isPositive = isPositive;
    }

    public String getProductName() {
        return productName;
    }

    public String getContent() {
        return content;
    }

    public boolean isPositive() {
        return isPositive;
    }
}