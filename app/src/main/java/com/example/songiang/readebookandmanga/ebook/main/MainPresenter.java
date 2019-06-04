package com.example.songiang.readebookandmanga.ebook.main;

import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.AppExecutors;
import com.example.songiang.readebookandmanga.utils.CancelDownloadCallback;
import com.example.songiang.readebookandmanga.utils.DownloadEbookTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainPresenter implements MainConstract.IPresenter, DownloadEbookTask.DownloadEbookCallback {


    private MainConstract.IView mView;
    private List<Ebook> data;
    public static boolean isLastPage;
    private CancelDownloadCallback mCallback;
    private AppExecutors executors;
    private ThreadPoolExecutor threadPoolExecutor;

    public MainPresenter() {
        data = new ArrayList<>();
        threadPoolExecutor = new ThreadPoolExecutor(3, 3, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void load(String url) {
        if (!isLastPage) {
            mView.showProgress();
            DownloadEbookTask downloadTask = new DownloadEbookTask(threadPoolExecutor, data, this);
            setmCallback(downloadTask);
            downloadTask.execute(url);

        }
    }

    public void setmCallback(CancelDownloadCallback mCallback) {
        this.mCallback = mCallback;
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
        cancelDownload();
        data.clear();
        isLastPage = false;
        load(url);

    }

    public void cancelDownload() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdownNow();
            threadPoolExecutor = new ThreadPoolExecutor(3, 3, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
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
