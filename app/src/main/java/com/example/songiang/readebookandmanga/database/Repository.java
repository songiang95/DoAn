package com.example.songiang.readebookandmanga.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.songiang.readebookandmanga.model.Chapter;
import com.example.songiang.readebookandmanga.model.ComicDownloaded;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.jvm.Synchronized;

public class Repository {

    private FavoriteComicDao favoriteComicDao;
    private FavoriteEbookDao favoriteEbookDao;
    private ChapterDao chapterDao;
    private DownloadComicDao downloadComicDao;
    private boolean isInsertComicSuccess;
    private boolean isInsertChapterSuccess;
    private List<ComicDownloaded> listComicDownloaded;
    private List<Chapter> listChapters;

    public Repository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        favoriteComicDao = db.favoriteComicDao();
        favoriteEbookDao = db.favoriteEbookDao();
        chapterDao = db.chapterDao();
        downloadComicDao = db.downloadComicDao();
    }

    public FavoriteComicDao getFavoriteComicDao() {
        return favoriteComicDao;
    }

    public FavoriteEbookDao getFavoriteEbookDao() {
        return favoriteEbookDao;
    }


    //----------------------------------------- ComicDownloadedActivity ---------------------------------------------
    public boolean insertDownloadedComic(final ComicDownloaded comicDownloaded) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadComicDao.insertDownloadedComic(comicDownloaded);
                    isInsertComicSuccess = true;
                } catch (Exception e) {
                    isInsertComicSuccess = false;
                }
            }
        });
        return isInsertComicSuccess;
    }


    public List<ComicDownloaded> getAllComicDownloaded() {
        listComicDownloaded = downloadComicDao.getAllDownloadedComic();
        Log.d("abba", "get list comic success: " + listComicDownloaded.toString());
        return listComicDownloaded;
    }

    //------------------------------------------- Chapter ------------------------------------------------------

    public boolean insertChapter(final Chapter chapter) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    chapterDao.insertChapter(chapter);
                    isInsertChapterSuccess = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isInsertChapterSuccess = false;
                }
            }
        });
        return isInsertChapterSuccess;
    }

    public List<Chapter> getAllChapters(final String comicName) {
        listChapters = chapterDao.getAllChapter(comicName);
        return listChapters;
    }
}
