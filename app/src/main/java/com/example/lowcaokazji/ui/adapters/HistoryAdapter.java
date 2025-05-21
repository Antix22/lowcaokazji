package com.example.lowcaokazji.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.data.HistoryEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryEntry> historyList = new ArrayList<>();

    public void submitList(List<HistoryEntry> history) {
        historyList = history != null ? history : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView textProductName, textShop, textPrice, textDate, textMsg;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.textHistoryProductName);
            textShop = itemView.findViewById(R.id.textHistoryShop);
            textPrice = itemView.findViewById(R.id.textHistoryPrice);
            textDate = itemView.findViewById(R.id.textHistoryDate);
            textMsg = itemView.findViewById(R.id.textHistoryMsg);
        }

        public void bind(HistoryEntry entry) {
            textProductName.setText(entry.productName);
            textShop.setText("Sklep: " + entry.shopName);
            textPrice.setText("Cena: " + String.format(Locale.getDefault(), "%.2f", entry.dealPrice) + " zł");

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(new Date(entry.timestamp));
            textDate.setText(formattedDate);

            textMsg.setText(entry.message != null ? entry.message : "");
        }
    }
}