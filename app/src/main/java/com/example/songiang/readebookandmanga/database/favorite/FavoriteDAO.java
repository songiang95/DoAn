package com.example.songiang.readebookandmanga.database.favorite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.songiang.readebookandmanga.model.Comic;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComicFavorite(Comic comic);

    @Delete
    void deleteFavoriteGrammar(Comic comic);

    @Query("SELECT * FROM favorite")
    List<Comic> getAllFavoriteComic();


}
