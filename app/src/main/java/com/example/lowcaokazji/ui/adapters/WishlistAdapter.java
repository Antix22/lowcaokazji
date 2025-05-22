package com.example.lowcaokazji.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.Product;

public class WishlistAdapter extends ListAdapter<Product, WishlistAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onProductBuy(Product product);
    }

    private final OnProductClickListener listener;

    public WishlistAdapter(OnProductClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Product>() {
                @Override
                public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                    return oldItem.name.equals(newItem.name)
                            && oldItem.category.equals(newItem.category)
                            && oldItem.targetPrice == newItem.targetPrice
                            && oldItem.url.equals(newItem.url)
                            && ((oldItem.imageUri == null && newItem.imageUri == null) || (oldItem.imageUri != null && oldItem.imageUri.equals(newItem.imageUri)))
                            && ((oldItem.description == null && newItem.description == null) || (oldItem.description != null && oldItem.description.equals(newItem.description)));
                }
            };

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItem(position);
        holder.bind(product, listener);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName, textCategory, textTargetPrice;
        private final Button buttonBuy;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textProductName);
            textCategory = itemView.findViewById(R.id.textProductCategory);
            textTargetPrice = itemView.findViewById(R.id.textProductTargetPrice);
            buttonBuy = itemView.findViewById(R.id.buttonBuy); // CRASH jeśli nie ma w layoucie!
        }

        public void bind(Product product, OnProductClickListener listener) {
            textName.setText(product.name);
            textCategory.setText(product.category);
            textTargetPrice.setText("Próg: " + product.targetPrice + " zł");

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onProductClick(product);
            });
            buttonBuy.setOnClickListener(v -> {
                if (listener != null)
                    listener.onProductBuy(product);
            });
        }
    }
}