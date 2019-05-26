package com.example.songiang.readebookandmanga.ebook.main;

import com.example.songiang.readebookandmanga.model.Ebook;

import java.util.List;

public interface MainConstract {

    interface IView {
        void showContent(List<Ebook> data);
        void showProgress();
        void hideProgress();

    }

    interface IPresenter {
        void load(String url);
        void attachView(IView v);
        void detachView();
    }

}
