package com.example.lowcaokazji.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.viewmodel.ProductViewModel;
import com.example.lowcaokazji.workers.PriceCheckWorker;

public class ProductDetailsActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        int productId = getIntent().getIntExtra("product_id", -1);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        Button buttonChangePrice = findViewById(R.id.buttonChangePrice);
        EditText editNewTargetPrice = findViewById(R.id.editNewTargetPrice);
        Button buttonSavePrice = findViewById(R.id.buttonSavePrice);

        editNewTargetPrice.setVisibility(View.GONE);
        buttonSavePrice.setVisibility(View.GONE);

        productViewModel.getAllProducts().observe(this, products -> {
            for (Product p : products) {
                if (p.id == productId) {
                    currentProduct = p;
                    displayProduct(p);
                    break;
                }
            }

            buttonChangePrice.setOnClickListener(v -> {
                if (currentProduct != null) {
                    editNewTargetPrice.setText(String.valueOf(currentProduct.targetPrice));
                    editNewTargetPrice.setVisibility(View.VISIBLE);
                    buttonSavePrice.setVisibility(View.VISIBLE);
                }
            });

            buttonSavePrice.setOnClickListener(v -> {
                if (currentProduct == null) return;
                String newPriceStr = editNewTargetPrice.getText().toString().replace(",", ".");
                if (newPriceStr.isEmpty()) {
                    Toast.makeText(this, "Podaj nową cenę!", Toast.LENGTH_SHORT).show();
                    return;
                }
                double newPrice;
                try {
                    newPrice = Double.parseDouble(newPriceStr);
                } catch (Exception e) {
                    Toast.makeText(this, "Nieprawidłowa cena", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentProduct.targetPrice = newPrice;
                productViewModel.update(currentProduct);

                Data data = new Data.Builder()
                        .putInt("productId", currentProduct.id)
                        .build();

                WorkManager.getInstance(this)
                        .enqueue(new OneTimeWorkRequest.Builder(PriceCheckWorker.class)
                                .setInputData(data)
                                .build());

                // Natychmiastowe sprawdzenie okazji i powiadomienie
                WorkManager.getInstance(this)
                        .enqueue(new OneTimeWorkRequest.Builder(PriceCheckWorker.class).build());

                Toast.makeText(this, "Cena docelowa zaktualizowana!", Toast.LENGTH_SHORT).show();
                displayProduct(currentProduct);
                editNewTargetPrice.setVisibility(View.GONE);
                buttonSavePrice.setVisibility(View.GONE);
            });
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