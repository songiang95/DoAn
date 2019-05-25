package com.example.songiang.readebookandmanga.utils;

import android.os.AsyncTask;

import com.example.songiang.readebookandmanga.main.Presenter;
import com.example.songiang.readebookandmanga.model.Comic;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DownloadMangaTask extends AsyncTask<String, Void, Void> {

    private List<Comic> data;
    private DownloadMangaCallback mCallback;

    public DownloadMangaTask(List data,DownloadMangaCallback callback)
    {
        mCallback = callback;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {


        try {
            Document doc = Jsoup.connect(strings[0]).get();
            if (doc!=null) {
                Elements elements = doc.select("div.update_item");
                for (Element sub : elements) {
                    Comic temp = new Comic();
                    Element urlSub = sub.getElementsByTag("a").first();
                    Element imgSub = sub.getElementsByTag("img").first();
                    if (urlSub != null) {
                        String url = "";
                        String title = urlSub.attr("title");
                        url = url + urlSub.attr("href");
                        temp.setDetailUrl(url);
                        temp.setName(title);
                    }
                    if (imgSub != null) {
                        String img = imgSub.attr("src");
                        if (img.contains("61x61")) {
                            img = img.replace("-61x61", "");
                        } else {
                            if (img.contains("jpg")) {
                                img = img.substring(0, img.length() - 11) + ".jpg";
                            }
                            else if(img.contains("png")) {
                                img = img.substring(0, img.length() - 11) + ".png";
                            }
                        }
                        temp.setImage(img);


                    }
                    data.add(temp);
                }
            }
        } catch (HttpStatusException e) {
            mCallback.isLastPage(true);
            e.printStackTrace();
        }catch (Exception e)
        {
            mCallback.isLastPage(true);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);
        mCallback.onFinishDownload(data);
    }

    public interface DownloadMangaCallback{
        void onFinishDownload(List<Comic> data);
        void isLastPage(boolean bool);
    }
}