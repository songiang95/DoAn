package com.example.songiang.readebookandmanga.database;

import android.content.Context;

import com.example.songiang.readebookandmanga.comic.main.MainContract;
import com.example.songiang.readebookandmanga.database.favorite.FavoriteDAO;
import com.example.songiang.readebookandmanga.model.Comic;

import java.util.List;

public class Repository {

    private FavoriteDAO favoriteDAO;

    public Repository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        favoriteDAO = db.favoriteDAO();
    }

    public FavoriteDAO getFavoriteDAO() {
        return favoriteDAO;
    }
}
