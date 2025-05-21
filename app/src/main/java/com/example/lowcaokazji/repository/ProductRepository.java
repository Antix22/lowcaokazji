package com.example.lowcaokazji.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.lowcaokazji.data.AppDatabase;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.data.WishlistDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private WishlistDao wishlistDao;
    private LiveData<List<Product>> allProducts;
    private ExecutorService executorService;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        wishlistDao = db.wishlistDao();
        allProducts = wishlistDao.getAll();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(final Product product) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wishlistDao.insert(product);
            }
        });
    }

    public void update(final Product product) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wishlistDao.update(product);
            }
        });
    }

    public void delete(final Product product) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wishlistDao.delete(product);
            }
        });
    }

    public void clearWishlist() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wishlistDao.clearWishlist();
            }
        });
    }
}