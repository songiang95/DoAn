package com.example.songiang.readebookandmanga.comic.detail;

import android.os.AsyncTask;
import android.util.Log;

import com.example.songiang.readebookandmanga.model.Comic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DetailPresenter implements DetailContract.IPresenter {

    private DetailContract.IView mView;
    private Comic mComic;

    @Override
    public void load(Comic comic) {
        mComic = comic;
        new DownloadDetailTask().execute(mComic.getDetailUrl());

    }

    @Override
    public void attachView(DetailContract.IView v) {
        mView = v;
    }

    @Override
    public void detachView() {
        mView = null;
    }


    private class DownloadDetailTask extends AsyncTask<String, Void, Void> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showProgress();
        }

        @Override
        protected Void doInBackground(String... strings) {


            try {
                Document doc = Jsoup.connect(strings[0]).get();
                if (doc != null) {
                    Elements chapContainer = doc.select("div.chapter-list");
                    Elements chapElements= chapContainer.select("div.row");
                    Elements infoElements = doc.select("ul.truyen_info_right");
                    for (Element chap : chapElements) {
                        Element chapSubject = chap.getElementsByTag("a").first();
                        if (chapSubject != null) {
                            String title = chapSubject.text();
                            String key = title.substring(title.lastIndexOf(" ") + 1);
                            String value = chapSubject.attr("href");
                            Log.d("abba", "doInBackground: key: " + key + "| value: " + value);
                            mComic.addChap(key, value);
                        }
                    }
                    for (Element info : infoElements) {

                        Element infoSubject = info.getElementsByTag("a").first();
                        if (infoSubject != null) {
                            String artist = infoSubject.attr("title");
                            Log.d("abba", "doInBackground: artist: " + artist);
                            mComic.setArtist(artist);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void value) {
            super.onPostExecute(value);
            if(mView!=null) {
                mView.hideProgress();
                mView.showContent(mComic);
            }
        }
    }
}
