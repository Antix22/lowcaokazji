package com.example.lowcaokazji.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.ui.adapters.WishlistAdapter;
import com.example.lowcaokazji.viewmodel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements WishlistAdapter.OnProductClickListener {

    private ProductViewModel productViewModel;
    private WishlistAdapter adapter;
    private ActivityResultLauncher<Intent> addProductLauncher;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            // Wyczyść dane logowania z preferencji
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Przejdź do ekranu logowania i wyczyść stos Activity
            Intent intent = new Intent(this, com.example.lowcaokazji.ui.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.wishlistRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WishlistAdapter(this);
        recyclerView.setAdapter(adapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> adapter.submitList(products));

        FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            addProductLauncher.launch(intent);
        });

        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Optionally refresh list or handle result from AddProductActivity
                }
        );

        findViewById(R.id.buttonHistory).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });

    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product_id", product.id);
        startActivity(intent);
    }

    @Override
    public void onProductBuy(Product product) {
        productViewModel.delete(product);

    }
}