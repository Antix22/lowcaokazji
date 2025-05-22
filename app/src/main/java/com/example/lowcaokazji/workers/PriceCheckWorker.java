package com.example.lowcaokazji.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.lowcaokazji.data.AppDatabase;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.data.WishlistDao;
import com.example.lowcaokazji.utils.JsonDataProvider;
import com.example.lowcaokazji.utils.NotificationHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PriceCheckWorker extends Worker {
    public PriceCheckWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        // Pobierz instancję bazy danych
        AppDatabase db = AppDatabase.getInstance(context);
        WishlistDao wishlistDao = db.wishlistDao();

        // Obsługa parametru productId (gdy chcemy sprawdzić tylko jeden produkt)
        int productId = getInputData().getInt("productId", -1);

        List<Product> productsToCheck;
        if (productId != -1) {
            Product p = wishlistDao.getProductById(productId);
            if (p == null) return Result.success();
            productsToCheck = Collections.singletonList(p);
            Log.d("PriceCheckWorker", "Sprawdzam tylko produkt id: " + productId);
        } else {
            productsToCheck = wishlistDao.getAllList();
            Log.d("PriceCheckWorker", "Sprawdzam wszystkie produkty, liczba: " + productsToCheck.size());
        }

        if (productsToCheck == null || productsToCheck.isEmpty()) {
            // Nic do sprawdzania
            return Result.success();
        }

        for (Product product : productsToCheck) {
            JsonDataProvider.PriceInfo priceInfo = JsonDataProvider.getPriceInfoForProduct(context, product.name);
            if (priceInfo == null) continue;

            // Szukaj najniższej ceny
            double minPrice = Double.MAX_VALUE;
            String bestShop = "";
            for (Map.Entry<String, Double> entry : priceInfo.getShopPrices().entrySet()) {
                if (entry.getValue() < minPrice) {
                    minPrice = entry.getValue();
                    bestShop = entry.getKey();
                }
            }

            // Sprawdź czy cena jest poniżej progu
            if (minPrice <= product.targetPrice) {
                int positive = JsonDataProvider.countPositiveReviews(context, product.name);
                int all = JsonDataProvider.countAllReviews(context, product.name);
                int percent = all > 0 ? (positive * 100 / all) : 0;

                String msg = "🔥 Okazja! \"" + product.name + "\" za " + minPrice + " zł w " + bestShop
                        + (percent >= 70 ? " z pozytywnymi opiniami!" : "!");
                NotificationHelper.showDealNotification(context, "Łowca Okazji", msg);

                db.historyDao().insert(new com.example.lowcaokazji.data.HistoryEntry(
                        product.name,
                        bestShop,
                        minPrice,
                        System.currentTimeMillis(),
                        msg
                ));
                Log.d("PriceCheckWorker", "Wysłano powiadomienie: " + msg);
            }
        }
        return Result.success();
    }
}