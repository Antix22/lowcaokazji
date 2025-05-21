package com.example.lowcaokazji.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lowcaokazji.data.HistoryEntry;
import com.example.lowcaokazji.repository.HistoryRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private final HistoryRepository repository;
    private final LiveData<List<HistoryEntry>> allHistory;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
        allHistory = repository.getAllHistory();
    }

    public LiveData<List<HistoryEntry>> getAllHistory() {
        return allHistory;
    }

    public void insert(HistoryEntry entry) {
        repository.insert(entry);
    }

    public void clearHistory() {
        repository.clearHistory();
    }
}