package com.example.songiang.readebookandmanga.ebook.search;

import com.example.songiang.readebookandmanga.comic.search.SearchContract;
import com.example.songiang.readebookandmanga.model.Ebook;

import java.util.List;

public interface SearchEbookContract {

    public interface IView {
        void showContent(List<Ebook> list);

        void showProgress();

        void hideProgress();
    }

    public interface IPresenter {
        void attachView(SearchEbookContract.IView view);

        void cancelDownload();

        void load(String url);

    }

}
