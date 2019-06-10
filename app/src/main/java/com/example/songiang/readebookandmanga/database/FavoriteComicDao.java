package com.example.songiang.readebookandmanga.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.songiang.readebookandmanga.model.Comic;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteComicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComicFavorite(Comic comic);

    @Delete
    void deleteFavoriteComic(Comic comic);

    @Query("SELECT * FROM favorite_comic")
    List<Comic> getAllFavoriteComic();


}
