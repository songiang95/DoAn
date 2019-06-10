package com.example.songiang.readebookandmanga.database;

import android.content.Context;

public class Repository {

    private FavoriteComicDao favoriteComicDao;
    private FavoriteEbookDao favoriteEbookDao;

    public Repository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        favoriteComicDao = db.favoriteComicDao();
        favoriteEbookDao = db.favoriteEbookDao();
    }

    public FavoriteComicDao getFavoriteComicDao() {
        return favoriteComicDao;
    }

    public FavoriteEbookDao getFavoriteEbookDao() {
        return favoriteEbookDao;
    }
}
