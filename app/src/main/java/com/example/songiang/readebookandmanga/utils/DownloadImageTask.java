package com.example.songiang.readebookandmanga.utils;

import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DownloadImageTask extends AsyncTask<String, Void, Void> {

    private List<String> data;
    private CallBack mCallback;
    private int chapterNumb;
    public DownloadImageTask(CallBack callBack)
    {
        mCallback = callBack;
        data = new ArrayList<>();
    }
    public DownloadImageTask(int chapterNumb)
    {
        data = new ArrayList<>();
        this.chapterNumb = chapterNumb;
    }

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
                raw = raw.substring(raw.indexOf("[") + 1, raw.indexOf("]") - 1);
                String[] rawArray = raw.split(",");
                for (String url : rawArray) {
                    url = url.replaceAll("[\"]", "");
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
        if(mCallback!=null)
        {
            mCallback.onDownloadFinish(data,chapterNumb);
        }
    }

    public interface CallBack{
        void onDownloadFinish(List<String> data, int chapterNumb);
    }

    public void setCallback(CallBack mCallback) {
        this.mCallback = mCallback;
    }
}