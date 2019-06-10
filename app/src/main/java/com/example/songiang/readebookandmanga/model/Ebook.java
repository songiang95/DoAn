package com.example.songiang.readebookandmanga.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_ebook")
public class Ebook {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mTitle;
    @ColumnInfo(name = "author")
    private String mAuthorName;
    @ColumnInfo(name = "pdf")
    private String mPdfLink;
    @ColumnInfo(name = "cover")
    private String mCover;

    @Ignore
    private String mDescription;
    @Ignore
    private String mBookType;
    @Ignore
    private String mUrl;


    public Ebook() {
    }

    public Ebook(String mTitle, String mAuthorName, String mDescription, String mBookType, String mCover, String url, String pdflink) {
        this.mTitle = mTitle;
        this.mAuthorName = mAuthorName;
        this.mDescription = mDescription;
        this.mBookType = mBookType;
        this.mCover = mCover;
        this.mUrl = url;
        this.mPdfLink = pdflink;
    }

    public String getPdfLink() {
        return mPdfLink;
    }

    public void setPdfLink(String mPdfLink) {
        this.mPdfLink = mPdfLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String mAuthorName) {
        this.mAuthorName = mAuthorName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmBookType() {
        return mBookType;
    }

    public void setmBookType(String mBookType) {
        this.mBookType = mBookType;
    }

    public String getCover() {
        return mCover;
    }

    public void setCover(String mCover) {
        this.mCover = mCover;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
