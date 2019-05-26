package com.example.songiang.readebookandmanga.comic.search;

import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.utils.DownloadMangaTask;
import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchContract.IPresenter, DownloadMangaTask.DownloadMangaCallback {


    private SearchContract.IView mView;
    private List<Comic> listData;
    public static boolean isLastPage;


    public SearchPresenter() {
        listData = new ArrayList<>();
    }

    @Override
    public void load(String url) {
        if (!isLastPage)
        {
            mView.showProgress();
            new DownloadMangaTask(listData,this).execute(url);
        }
    }

    @Override
    public void attachView(SearchContract.IView view) {
        mView = view;

    }

    @Override
    public void detachView() {

    }


    @Override
    public void onFinishDownload(List<Comic> data) {
        mView.hideProgress();
        if(data!=null)
        {
            mView.showContent(data);
        }
    }

    @Override
    public void isLastPage(boolean bool) {

    }
}
