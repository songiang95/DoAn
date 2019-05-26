package com.example.songiang.readebookandmanga.comic.reading;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ReadingPresenter implements ReadingConstract.IPresenter {

    private ReadingConstract.IView mView;
    private List<String> data;

    public ReadingPresenter() {
        data = new ArrayList<>();
    }

    @Override
    public void load(String url) {
        new DownloadImageTask().execute(url);
    }

    @Override
    public void attachView(ReadingConstract.IView v) {
        this.mView = v;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect(strings[0]).get();
                if (doc != null) {
                    Elements imgElements = doc.select("div.vung_doc");
                    String raw = imgElements.html();
                    raw = raw.substring(raw.indexOf("["));
                    String[] rawArray = raw.split(",");
                    for (String url : rawArray) {
                        url = url.substring(1, url.length() - 1);
                        data.add(url);
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mView != null)
                mView.showContent(data);
            Log.d("link", "onPostExecute: img link:  "+ data);
        }
    }


}
