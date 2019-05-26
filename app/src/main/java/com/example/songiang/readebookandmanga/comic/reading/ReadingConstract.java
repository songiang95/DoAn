package com.example.songiang.readebookandmanga.comic.reading;

import java.util.List;

public interface ReadingConstract {


    interface IView{
        void showContent(List<String> data);
        void showProgress();
        void hideProgress();


    }

    interface IPresenter {
        void load(String url);
        void attachView(ReadingConstract.IView v);
        void detachView();
    }

}
