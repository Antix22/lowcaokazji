package com.example.lowcaokazji.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.lowcaokazji.data.AppDatabase;
import com.example.lowcaokazji.data.HistoryDao;
import com.example.lowcaokazji.data.HistoryEntry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryRepository {

    private final HistoryDao historyDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public HistoryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        historyDao = db.historyDao();
    }

    public LiveData<List<HistoryEntry>> getAllHistoryForUser(String username) {
        return historyDao.getAllForUser(username);
    }

    public void insert(HistoryEntry entry) {
        executor.execute(() -> historyDao.insert(entry));
    }

    public void clearHistoryForUser(String username) {
        executor.execute(() -> historyDao.clearHistoryForUser(username));
    }
}