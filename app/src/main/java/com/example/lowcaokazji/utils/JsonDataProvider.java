package com.example.lowcaokazji.utils;

import android.content.Context;

import com.example.lowcaokazji.model.PriceInfo;
import com.example.lowcaokazji.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonDataProvider {

    // Pobiera info o cenach danego produktu z mockowanego pliku JSON
    public static PriceInfo getPriceInfoForProduct(Context context, String productName) {
        try {
            String jsonStr = loadJSONFromRaw(context, "prices");
            if (jsonStr == null) return null;
            JSONObject json = new JSONObject(jsonStr);

            if (!json.has(productName)) return null;

            JSONObject shops = json.getJSONObject(productName);
            PriceInfo priceInfo = new PriceInfo(productName);

            Iterator<String> keys = shops.keys();
            while (keys.hasNext()) {
                String shop = keys.next();
                double price = shops.getDouble(shop);
                priceInfo.addShopPrice(shop, price);
            }
            return priceInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Pobiera listę opinii produktu z mockowanego pliku JSON
    public static List<Review> getReviewsForProduct(Context context, String productName) {
        List<Review> reviews = new ArrayList<>();
        try {
            String jsonStr = loadJSONFromRaw(context, "reviews");
            if (jsonStr == null) return reviews;
            JSONObject json = new JSONObject(jsonStr);

            if (!json.has(productName)) return reviews;

            JSONArray arr = json.getJSONArray(productName);
            for (int i = 0; i < arr.length(); i++) {
                String content = arr.getString(i);
                // Prosta analiza sentymentu: jeśli zawiera "super", "najlepsze", "polecam" itp. to pozytywna
                boolean positive = content.toLowerCase().contains("super") || content.toLowerCase().contains("najlepsze") || content.toLowerCase().contains("polecam") || content.toLowerCase().contains("warto");
                reviews.add(new Review(productName, content, positive));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // Zwraca liczbę pozytywnych opinii
    public static int countPositiveReviews(Context context, String productName) {
        List<Review> reviews = getReviewsForProduct(context, productName);
        int count = 0;
        for (Review r : reviews) if (r.isPositive()) count++;
        return count;
    }

    // Zwraca liczbę wszystkich opinii
    public static int countAllReviews(Context context, String productName) {
        return getReviewsForProduct(context, productName).size();
    }

    // Helper do wczytywania JSON z /res/raw/
    private static String loadJSONFromRaw(Context context, String fileNameNoExt) {
        try {
            int resId = context.getResources().getIdentifier(fileNameNoExt, "raw", context.getPackageName());
            if (resId == 0) return null;
            InputStream is = context.getResources().openRawResource(resId);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}