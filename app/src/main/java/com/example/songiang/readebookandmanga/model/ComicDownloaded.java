package com.example.songiang.readebookandmanga.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.songiang.readebookandmanga.utils.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "downloaded_comic")
public class ComicDownloaded implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "author")
    private String mAuthor;
    @Ignore
    private List<Chapter> mChaptersDownloaded;

    public ComicDownloaded() {
        mChaptersDownloaded = new ArrayList<>();
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setChaptersDownloaded(List<Chapter> chapters) {
        mChaptersDownloaded = chapters;
    }

    public List getChaptersDownloaded() {
        return mChaptersDownloaded;
    }

    public void add(Chapter chapter) {
        mChaptersDownloaded.add(chapter);
    }

    public void remove(Chapter chapter) {
        mChaptersDownloaded.remove(chapter);
    }
}
