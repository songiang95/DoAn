package com.example.songiang.readebookandmanga.comic.search;

import com.example.songiang.readebookandmanga.model.Comic;

import java.util.List;

public interface SearchContract {

    interface IView{
        void showContent(List<Comic> list);
        void showProgress();
        void hideProgress();
    }

    interface IPresenter{
        void load(String url);
        void attachView(SearchContract.IView view);
        void detachView();
    }

}
