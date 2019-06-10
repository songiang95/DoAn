package com.example.songiang.readebookandmanga.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.model.Ebook;

@Database(entities = {Comic.class, Ebook.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    abstract FavoriteComicDao favoriteComicDao();
    abstract FavoriteEbookDao favoriteEbookDao();

    private static volatile AppDatabase INSTANCE;

    static final Migration MIGRATION_1_2  = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE favorite_ebook (id TEXT,author TEXT,pdf TEXT, cover TEXT)");
        }
    };

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
