package com.example.songiang.readebookandmanga.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.songiang.readebookandmanga.database.favorite.FavoriteDAO;
import com.example.songiang.readebookandmanga.model.Comic;

@Database(entities = {Comic.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    abstract FavoriteDAO favoriteDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
