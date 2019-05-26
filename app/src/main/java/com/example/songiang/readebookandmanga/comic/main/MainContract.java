package com.example.songiang.readebookandmanga.comic.main;

import com.example.songiang.readebookandmanga.model.Comic;

import java.util.List;

public interface MainContract {
    interface IView {
        void showContent(List<Comic> listData);
        void hideContent();
        void showProgress();
        void hideProgress();
        void showToastLastPage();

    }

    interface IPresenter {

        void load(String url);
        void loadMore(String url);
        void reLoad(String url);
        boolean isLastPage();
        void attachView(MainContract.IView v);
    }
}
