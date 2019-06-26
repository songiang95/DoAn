package com.example.songiang.readebookandmanga.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.songiang.readebookandmanga.model.Chapter;

import java.util.List;

@Dao
public interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChapter(Chapter chapter);

    @Query("SELECT * FROM chapter WHERE comic_name =:comicName")
    List<Chapter> getAllChapter(String comicName);
}
