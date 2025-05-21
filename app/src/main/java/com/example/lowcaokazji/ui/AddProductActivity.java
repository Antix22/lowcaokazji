package com.example.lowcaokazji.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.Product;
import com.example.lowcaokazji.model.PriceInfo;
import com.example.lowcaokazji.repository.ProductRepository;
import com.example.lowcaokazji.utils.JsonDataProvider;
import com.example.lowcaokazji.utils.NotificationHelper;
import com.example.lowcaokazji.viewmodel.ProductViewModel;

import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private EditText editName, editUrl, editCategory, editImage, editTargetPrice, editDescription;
    private Button btnAdd;
    private ProductViewModel productViewModel;

    private ProductRepository productRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editName = findViewById(R.id.editProductName);
        editUrl = findViewById(R.id.editProductUrl);
        editCategory = findViewById(R.id.editProductCategory);
        editImage = findViewById(R.id.editProductImage);
        editTargetPrice = findViewById(R.id.editProductTargetPrice);
        editDescription = findViewById(R.id.editProductDescription);
        btnAdd = findViewById(R.id.buttonAddProduct);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        btnAdd.setOnClickListener(v -> addProduct());

        productRepository = new ProductRepository(getApplication());
    }

    private void addProduct() {
        String name = editName.getText().toString().trim();
        String url = editUrl.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String image = editImage.getText().toString().trim();
        String targetPriceStr = editTargetPrice.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url) || TextUtils.isEmpty(category) || TextUtils.isEmpty(targetPriceStr)) {
            Toast.makeText(this, "Wypełnij wymagane pola!", Toast.LENGTH_SHORT).show();
            return;
        }

        double targetPrice;
        try {
            targetPrice = Double.parseDouble(targetPriceStr.replace(',', '.'));
        } catch (Exception e) {
            Toast.makeText(this, "Nieprawidłowa cena", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- ORYGINALNY KOD DODAWANIA PRODUKTU ---
        Product product = new Product(name, url, category, image, targetPrice, description);
        productRepository.insert(product);

        // --- NOWA CZĘŚĆ: NATYCHMIASTOWE POWIADOMIENIE O NAJNIŻSZEJ CENIE ---
        PriceInfo priceInfo = JsonDataProvider.getPriceInfoForProduct(this, name);
        if (priceInfo != null && !priceInfo.getShopPrices().isEmpty()) {
            double minPrice = Double.MAX_VALUE;
            String bestShop = "";
            for (Map.Entry<String, Double> entry : priceInfo.getShopPrices().entrySet()) {
                if (entry.getValue() < minPrice) {
                    minPrice = entry.getValue();
                    bestShop = entry.getKey();
                }
            }
            // Sprawdź czy cena jest poniżej progu
            if (minPrice <= targetPrice) {
                // Pobierz opinie i policz ile pozytywnych (symulacja)
                int positive = JsonDataProvider.countPositiveReviews(this, name);
                int all = JsonDataProvider.countAllReviews(this, name);
                int percent = all > 0 ? (positive * 100 / all) : 0;

                String msg = "🔥 Okazja! \"" + name + "\" za " + minPrice + " zł w " + bestShop
                        + (percent >= 70 ? " z pozytywnymi opiniami!" : "!");
                NotificationHelper.showDealNotification(this, "Łowca Okazji", msg);
            }
        }

        Toast.makeText(this, "Dodano produkt!", Toast.LENGTH_SHORT).show();
        finish();
    }

}