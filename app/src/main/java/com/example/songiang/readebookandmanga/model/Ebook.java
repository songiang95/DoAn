package com.example.songiang.readebookandmanga.model;

public class Ebook {

    private String mTitle;
    private String mAuthorName;
    private String mDescription;
    private String mBookType;
    private String mCover;
    private String mUrl;
    private String mPdfLink;

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

    public String getmPdfLink() {
        return mPdfLink;
    }

    public void setmPdfLink(String mPdfLink) {
        this.mPdfLink = mPdfLink;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public void setmAuthorName(String mAuthorName) {
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

    public String getmCover() {
        return mCover;
    }

    public void setmCover(String mCover) {
        this.mCover = mCover;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
