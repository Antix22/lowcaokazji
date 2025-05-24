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
import com.example.lowcaokazji.utils.JsonDataProvider;
import com.example.lowcaokazji.utils.NotificationHelper;
import com.example.lowcaokazji.viewmodel.ProductViewModel;

import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private EditText editName, editUrl, editCategory, editTargetPrice;
    private Button btnAdd;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editName = findViewById(R.id.editProductName);
        editUrl = findViewById(R.id.editProductUrl);
        editCategory = findViewById(R.id.editProductCategory);
        editTargetPrice = findViewById(R.id.editProductTargetPrice);
        btnAdd = findViewById(R.id.buttonAddProduct);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        btnAdd.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String name = editName.getText().toString().trim();
        String url = editUrl.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String targetPriceStr = editTargetPrice.getText().toString().trim();

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

        Product product = new Product(name, url, category, targetPrice);
        productViewModel.insert(product);

        // Od razu sprawdź czy jest okazja i wyślij powiadomienie
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
            if (minPrice <= targetPrice) {
                String msg = "🔥 Okazja! \"" + name + "\" za " + minPrice + " zł w " + bestShop + "!";
                NotificationHelper.showDealNotification(this, "Łowca Okazji", msg);
            }
        }

        Toast.makeText(this, "Produkt dodany!", Toast.LENGTH_SHORT).show();
        finish();
    }
}