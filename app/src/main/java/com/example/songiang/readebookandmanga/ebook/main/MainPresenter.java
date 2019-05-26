package com.example.songiang.readebookandmanga.ebook.main;

import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.DownloadEbookTask;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainConstract.IPresenter, DownloadEbookTask.DownloadEbookCallback {


    private MainConstract.IView mView;
    private List<Ebook> data;
    public static boolean isLastPage;

    public MainPresenter() {
        data = new ArrayList<>();
    }

    @Override
    public void load(String url) {
        if (!isLastPage) {
            mView.showProgress();
            new DownloadEbookTask(data, this).execute(url);
        }
    }

    @Override
    public void attachView(MainConstract.IView v) {
        mView = v;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void reLoad(String url) {
        data.clear();
        load(url);
    }

    @Override
    public void onFinishDownload(List<Ebook> data) {
        if (data != null) {
            mView.hideProgress();
            mView.showContent(data);
        }
    }

    @Override
    public void isLastPage(boolean bool) {
        isLastPage = bool;
    }
}
