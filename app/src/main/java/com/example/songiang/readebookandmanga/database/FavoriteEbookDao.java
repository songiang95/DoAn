package com.example.songiang.readebookandmanga.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.songiang.readebookandmanga.model.Ebook;

import java.util.List;

@Dao
public interface FavoriteEbookDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEbookFavorite(Ebook ebook);

    @Delete
    void deleteFavoriteEbook(Ebook ebook);

    @Query("SELECT * FROM favorite_ebook")
    List<Ebook> getAllFavoriteEbook();
}
