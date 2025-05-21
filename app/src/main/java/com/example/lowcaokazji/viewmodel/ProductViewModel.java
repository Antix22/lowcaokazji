package com.example.lowcaokazji.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.repository.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final ProductRepository repository;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        repository.insert(product);
    }

    public void update(Product product) {
        repository.update(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void clearWishlist() {
        repository.clearWishlist();
    }
}