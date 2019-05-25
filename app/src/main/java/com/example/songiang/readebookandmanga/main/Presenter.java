package com.example.songiang.readebookandmanga.main;

import android.util.Log;
import android.widget.Toast;

import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.utils.DownloadMangaTask;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements MainContract.IPresenter, DownloadMangaTask.DownloadMangaCallback {

    private final String TAG = this.getClass().getSimpleName();
    private MainContract.IView mView;
    private List<Comic> listData;
    private boolean isLastPage = false;

    public Presenter() {
        listData = new ArrayList<>();

    }

    @Override
    public boolean isLastPage() {
        return isLastPage;
    }


    @Override
    public void load(String url) {
        if (!isLastPage) {
            mView.showProgress();
            new DownloadMangaTask(listData, this).execute(url);
        }
    }

    @Override
    public void loadMore(String url) {
        load(url);
    }

    @Override
    public void reLoad(String url) {
        listData.clear();
        isLastPage = false;
        load(url);
    }

    @Override
    public void attachView(MainContract.IView v) {
        mView = v;
    }

    @Override
    public void onFinishDownload(List<Comic> data) {
        mView.hideProgress();
        if (data != null)
            mView.showContent(data);
        else
            Log.e(TAG, "onFinishDownload: data is null");

    }

    @Override
    public void isLastPage(boolean bool) {
        isLastPage = bool;
    }

}
