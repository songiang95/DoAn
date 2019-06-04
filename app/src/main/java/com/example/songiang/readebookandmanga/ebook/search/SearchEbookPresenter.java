package com.example.songiang.readebookandmanga.ebook.search;

import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.CancelDownloadCallback;
import com.example.songiang.readebookandmanga.utils.DownloadEbookTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchEbookPresenter implements SearchEbookContract.IPresenter, DownloadEbookTask.DownloadEbookCallback {

    private SearchEbookContract.IView mView;
    private List<Ebook> data;
    public static boolean isLastPage;
    private CancelDownloadCallback mCallback;
    private ThreadPoolExecutor threadPoolExecutor;

    public SearchEbookPresenter() {
        data = new ArrayList<>();
    }


    public void setCallback(CancelDownloadCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void attachView(SearchEbookContract.IView view) {
        this.mView = view;
    }

    @Override
    public void load(String url) {
        if (!isLastPage) {
            threadPoolExecutor = new ThreadPoolExecutor(3,3,0, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
            mView.showProgress();
            url.replace(" ", "+");
            String searchUrl = "https://sachvui.com/search/?tu-khoa=" + url;
            DownloadEbookTask downloadEbookTask = new DownloadEbookTask(threadPoolExecutor,data, this);
            downloadEbookTask.execute(searchUrl);
            setCallback(downloadEbookTask);
        }
    }

    @Override
    public void cancelDownload() {
        if (mCallback != null) {
            mCallback.onCancel();
        }
    }


    @Override
    public void onFinishDownload(List<Ebook> data) {
        if (data != null) {
            mView.showContent(data);
            mView.hideProgress();
        }
        else{
            mView.showError();
        }
    }

    @Override
    public void isLastPage(boolean bool) {
        isLastPage = bool;
    }
}
