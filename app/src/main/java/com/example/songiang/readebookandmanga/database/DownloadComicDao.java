package com.example.songiang.readebookandmanga.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.songiang.readebookandmanga.model.ComicDownloaded;

import java.util.List;

@Dao
public interface DownloadComicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDownloadedComic(ComicDownloaded comicDownloaded);

    @Query("SELECT * FROM downloaded_comic")
    List<ComicDownloaded> getAllDownloadedComic();

}
