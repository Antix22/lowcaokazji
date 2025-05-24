package com.example.lowcaokazji.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.model.PriceInfo;
import com.example.lowcaokazji.model.Review;
import com.example.lowcaokazji.utils.JsonDataProvider;
import com.example.lowcaokazji.viewmodel.ProductViewModel;

import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        int productId = getIntent().getIntExtra("product_id", -1);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        productViewModel.getAllProducts().observe(this, products -> {
            for (Product p : products) {
                if (p.id == productId) {
                    currentProduct = p;
                    displayProduct(p);
                    break;
                }
            }
        });
    }

    private void displayProduct(Product product) {
        ((TextView) findViewById(R.id.textProductName)).setText(product.name);
        ((TextView) findViewById(R.id.textProductCategory)).setText(product.category);
        ((TextView) findViewById(R.id.textProductPrice)).setText("Cena docelowa: " + product.targetPrice + " zł");


        //       ((TextView) findViewById(R.id.textProductUrl)).setText(product.url);
 //       ((TextView) findViewById(R.id.textProductDescription)).setText(product.description);

        List<Review> reviews = JsonDataProvider.getReviewsForProduct(this, product.name);
        StringBuilder reviewsText = new StringBuilder();
        if (reviews != null && !reviews.isEmpty()) {
            for (Review review : reviews) {
                reviewsText.append("• ").append(review.getContent()).append("\n\n");
            }
        } else {
            reviewsText.append("Brak opinii.");
        }
        ((TextView) findViewById(R.id.textProductDescription)).setText(reviewsText.toString());

        PriceInfo priceInfo = JsonDataProvider.getPriceInfoForProduct(this, product.name);
        StringBuilder pricesText = new StringBuilder();

        ((TextView) findViewById(R.id.textProductShops)).setText(pricesText.toString());
        if (priceInfo != null && priceInfo.getShopPrices() != null && !priceInfo.getShopPrices().isEmpty()) {
            for (Map.Entry<String, Double> entry : priceInfo.getShopPrices().entrySet()) {
                pricesText.append(entry.getKey()).append(": ").append(String.format("%.2f zł", entry.getValue())).append("\n");
            }
        } else {
            pricesText.append("Brak danych o cenach.");
        }
        ((TextView) findViewById(R.id.textProductShops)).setText(pricesText.toString());
    }
    }


