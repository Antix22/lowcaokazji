package com.example.lowcaokazji.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowcaokazji.R;
import com.example.lowcaokazji.ui.adapters.HistoryAdapter;
import com.example.lowcaokazji.viewmodel.HistoryViewModel;

public class HistoryActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        historyViewModel.getAllHistory().observe(this, entries -> adapter.submitList(entries));
    }
}