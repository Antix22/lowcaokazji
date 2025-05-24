package com.example.lowcaokazji.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class, HistoryEntry.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract WishlistDao wishlistDao();
    public abstract HistoryDao historyDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "okazje_db")
                            .addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration() // możesz usunąć na produkcji!
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // MIGRACJA: dodanie kolumny username do history
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE history ADD COLUMN username TEXT");
        }
    };
}