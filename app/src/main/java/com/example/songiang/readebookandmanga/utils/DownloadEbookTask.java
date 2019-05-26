package com.example.songiang.readebookandmanga.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.songiang.readebookandmanga.model.Ebook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


public class DownloadEbookTask extends AsyncTask<String, Void, Void> {

    private final String TAG = "abba";
    private List<Ebook> data;
    private DownloadEbookCallback mCallback;

    public DownloadEbookTask(List data, DownloadEbookCallback callback) {
        this.data = data;
        this.mCallback = callback;
    }

    @Override
    protected Void doInBackground(String... strings) {

        try {
            Document doc = Jsoup.connect(strings[0]).get();
            if (doc != null) {
                Elements elements = doc.select("div.ebook");
                for (Element element : elements) {
                    Ebook ebook = new Ebook();
                    Element urlSubject = element.getElementsByTag("a").first();
                    Element imgSubject = element.getElementsByTag("img").first();
                    if (urlSubject != null) {
                        ebook.setmUrl(urlSubject.attr("href"));
                        Log.d(TAG, "doInBackground: ebook url:  " + urlSubject.attr("href"));
                        new DownloadEbookDetailTask(ebook).execute(ebook.getmUrl());
                    }
                    if (imgSubject != null) {
                        ebook.setmCover(imgSubject.attr("src"));
                        ebook.setmTitle(imgSubject.attr("alt"));
                        Log.d(TAG, "doInBackground: ebook img:  " + imgSubject.attr("src"));
                        Log.d(TAG, "doInBackground: ebook title:  " + imgSubject.attr("alt"));
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.isLastPage(true);
            }
        }


        return null;
    }

    private class DownloadEbookDetailTask extends AsyncTask<String, Void, Void> {

        private Ebook ebook;

        public DownloadEbookDetailTask(Ebook ebook) {
            this.ebook = ebook;
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                Document doc = Jsoup.connect(strings[0]).get();
                if (doc != null) {
                    Elements elements = doc.select("div.col-md-8");
                    Elements elementsDescription = doc.select("div.gioi_thieu_sach");
                    if (elementsDescription != null) {
                        for (Element element : elementsDescription) {
                            Element description = element.getElementsByTag("p").first();
                            String bookDescription = description.text();
                            ebook.setmDescription(bookDescription);
                            Log.d(TAG, "doInBackground: description "+bookDescription);
                        }
                    }

                    for (Element element : elements) {

                        Element authorSubject = element.getElementsByTag("h5").first();
                        Element pdfSubject = element.getElementsByClass("btn-danger").first();
                        if (authorSubject != null) {
                            ebook.setmAuthorName(authorSubject.text());
                            Log.d(TAG, "doInBackground: ebook author:  " + authorSubject.text());
                        }
                        if (pdfSubject != null) {
                            ebook.setmPdfLink(pdfSubject.attr("href"));
                            Log.d(TAG, "doInBackground: ebook pdf link:  " + pdfSubject.attr("href"));
                        }
                        data.add(ebook);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.isLastPage(true);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mCallback != null) {
                mCallback.onFinishDownload(data);
            }
        }
    }


    public interface DownloadEbookCallback {
        void onFinishDownload(List<Ebook> data);

        void isLastPage(boolean bool);
    }

}
