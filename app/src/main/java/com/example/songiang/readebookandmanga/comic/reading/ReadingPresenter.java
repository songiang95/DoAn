package com.example.songiang.readebookandmanga.comic.reading;

import android.os.AsyncTask;
import android.util.Log;

import com.example.songiang.readebookandmanga.utils.DownloadImageTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ReadingPresenter implements ReadingConstract.IPresenter,DownloadImageTask.CallBack {

    private ReadingConstract.IView mView;

    public ReadingPresenter() {
    }

    @Override
    public void load(String url) {
        new DownloadImageTask(this).execute(url);
    }

    @Override
    public void attachView(ReadingConstract.IView v) {
        this.mView = v;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }


    @Override
    public void onDownloadFinish(List list,int i) {
        mView.showContent(list);
    }
}
