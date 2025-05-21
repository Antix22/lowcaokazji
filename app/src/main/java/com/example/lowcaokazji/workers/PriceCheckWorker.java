package com.example.lowcaokazji.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.lowcaokazji.data.AppDatabase;
import com.example.lowcaokazji.data.HistoryEntry;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.data.WishlistDao;
import com.example.lowcaokazji.utils.JsonDataProvider;
import com.example.lowcaokazji.utils.NotificationHelper;
import com.example.lowcaokazji.model.PriceInfo;

import java.util.List;
import java.util.Map;

// Worker codziennie sprawdzający okazje i powiadamiający użytkownika
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
        List<Product> wishlist = wishlistDao.getAll().getValue(); // Uwaga: getValue() działa tylko, jeśli LiveData jest aktywna (wątpliwe w tle)

        // Rozwiązanie: pobierz produkty synchronizacyjnie (trzeba dodać metodę do DAO - np. getAllList())
        if (wishlist == null || wishlist.isEmpty()) {
            // Nic do sprawdzania
            return Result.success();
        }

        // Dla każdego produktu na wishliście
        for (Product product : wishlist) {
            // Pobierz ceny ze sklepu (symulacja/mock)
            PriceInfo priceInfo = JsonDataProvider.getPriceInfoForProduct(context, product.name);
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
                // Pobierz opinie i policz ile pozytywnych (symulacja)
                int positive = JsonDataProvider.countPositiveReviews(context, product.name);
                int all = JsonDataProvider.countAllReviews(context, product.name);
                int percent = all > 0 ? (positive * 100 / all) : 0;

                // Powiadomienie
                String msg = "🔥 Okazja! \"" + product.name + "\" za " + minPrice + " zł w " + bestShop
                        + (percent >= 70 ? " z pozytywnymi opiniami!" : "!");
                NotificationHelper.showDealNotification(context, "Łowca Okazji", msg);

                // Zapisz do historii
                db.historyDao().insert(new HistoryEntry(
                        product.name,
                        bestShop,
                        minPrice,
                        System.currentTimeMillis(),
                        msg
                ));
            }
        }

        return Result.success();
    }
}