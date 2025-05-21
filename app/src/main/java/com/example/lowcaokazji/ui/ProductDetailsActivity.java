package com.example.lowcaokazji.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.viewmodel.ProductViewModel;

public class ProductDetailsActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        int productId = getIntent().getIntExtra("product_id", -1);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // For simplicity: assuming you have a method to get a product by ID synchronously (for full implementation, consider using a separate query)
        productViewModel.getAllProducts().observe(this, products -> {
            for (Product p : products) {
                if (p.id == productId) {
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
        ((TextView) findViewById(R.id.textProductUrl)).setText(product.url);
        ((TextView) findViewById(R.id.textProductDescription)).setText(product.description);

        // Możesz tutaj dodać wyświetlanie ceny z mockowanych sklepów, opinii, kodu rabatowego itd.
    }
}