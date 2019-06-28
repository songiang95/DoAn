package com.example.songiang.readebookandmanga.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chapter")
public class Chapter {


    @ColumnInfo(name = "comic_name")
    private String mComicTitle;
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "chapter_number")
    private int mChapterNumb;

    public Chapter() {
    }

    public Chapter(String title, int numb) {
        mComicTitle = title;
        mChapterNumb = numb;
    }

    public String getComicTitle() {
        return mComicTitle;
    }

    public void setComicTitle(String mComicTitle) {
        this.mComicTitle = mComicTitle;
    }

    public int getChapterNumb() {
        return mChapterNumb;
    }

    public void setChapterNumb(int mChapterNumb) {
        this.mChapterNumb = mChapterNumb;
    }

}
