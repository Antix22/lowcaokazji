package com.example.lowcaokazji.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WishlistDao {
    @Query("SELECT * FROM wishlist")
    LiveData<List<Product>> getAll();

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM wishlist")
    void clearWishlist();
}