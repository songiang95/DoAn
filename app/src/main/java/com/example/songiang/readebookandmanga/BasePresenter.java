package com.example.songiang.readebookandmanga;

import android.view.View;

import com.example.songiang.readebookandmanga.main.MainContract;

public interface BasePresenter {

    void attachView(View v);
    void detachView();

}
