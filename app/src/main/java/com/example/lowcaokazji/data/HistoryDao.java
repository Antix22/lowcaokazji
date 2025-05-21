package com.example.lowcaokazji.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insert(HistoryEntry entry);

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    LiveData<List<HistoryEntry>> getAll();

    @Query("DELETE FROM history")
    void clearHistory();
}