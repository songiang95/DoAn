package com.example.songiang.readebookandmanga.main;

import android.os.AsyncTask;

import com.example.songiang.readebookandmanga.model.Comic;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements MainContract.IPresenter {

    private MainContract.IView mView;
    private List<Comic> listData;
    private boolean isLastPage = false;

    public Presenter() {
        listData = new ArrayList<>();

    }

    @Override
    public boolean isLastPage() {
        return isLastPage;
    }



    @Override
    public void load(String url) {
        new DownloadMainTask().execute(url);
    }

    @Override
    public void loadMore(String url) {
        load(url);
    }

    @Override
    public void reLoad(String url) {
        listData.clear();
        isLastPage = false;
        load(url);
    }

    @Override
    public void attachView(MainContract.IView v) {
        mView = v;
    }


    public class DownloadMainTask extends AsyncTask<String, Void, List<Comic>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showProgress();
        }

        @Override
        protected List doInBackground(String... strings) {


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
                        listData.add(temp);
                    }
                }
            } catch (HttpStatusException e) {
                isLastPage = true;
                e.printStackTrace();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return listData;
        }

        @Override
        protected void onPostExecute(List<Comic> comics) {
            super.onPostExecute(comics);
            mView.hideProgress();
            mView.showContent(comics);
        }
    }
}
