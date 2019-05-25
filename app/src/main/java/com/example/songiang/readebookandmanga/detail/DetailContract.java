package com.example.songiang.readebookandmanga.detail;

import com.example.songiang.readebookandmanga.BasePresenter;
import com.example.songiang.readebookandmanga.model.Comic;

public interface DetailContract {

    interface IView{
        void showContent(Comic comic);
        void showProgress();
        void hideProgress();

    }

    interface IPresenter {
        void load(Comic comic);
        void attachView(DetailContract.IView v);
        void detachView();
    }

}
