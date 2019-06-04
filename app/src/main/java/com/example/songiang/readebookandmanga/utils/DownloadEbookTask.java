package com.example.songiang.readebookandmanga.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.observer.Observer;
import com.example.songiang.readebookandmanga.observer.Subject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ThreadPoolExecutor;


public class DownloadEbookTask extends AsyncTask<String, Void, Void> implements CancelDownloadCallback, Subject {

    private final String TAG = "abba";
    private List<Ebook> data;
    private DownloadEbookCallback mCallback;
    private List<Observer> listTask;
    private AppExecutors executors;
    ThreadPoolExecutor threadPoolExecutor;

    public DownloadEbookTask(ThreadPoolExecutor threadPoolExecutor,List data, DownloadEbookCallback callback) {
        this.data = data;
        this.mCallback = callback;
        listTask = new ArrayList<>();
        executors = AppExecutors.getInstance();
        this.threadPoolExecutor  = threadPoolExecutor;
    }

    @Override
    protected Void doInBackground(String... strings) {

        if (!isCancelled()) {
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
                            DownloadDetail downloadTask = new DownloadDetail(ebook);
                            threadPoolExecutor.execute(downloadTask);
                            Log.d(TAG, "doInBackground: add download task");

                        }
                        if (imgSubject != null) {
                            ebook.setmCover(imgSubject.attr("src"));
                            ebook.setmTitle(imgSubject.attr("alt"));
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.isLastPage(true);
                }
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    @Override
    public void onCancel() {
        notifyObservers();
        this.cancel(true);
    }

    @Override
    public void register(Observer observer) {
        if (!listTask.contains(observer)) {
            listTask.add(observer);
        }
    }

    @Override
    public void remove(Observer observer) {
        listTask.remove(observer);

    }

    @Override
    public void notifyObservers() {
        for (Observer observer : listTask) {
            observer.update(true);
        }
    }


    private class DownloadDetail implements Runnable{
        private Ebook mEbook;
        public DownloadDetail(Ebook ebook){
            mEbook = ebook;
        }
        @Override
        public void run() {

            if (!Thread.currentThread().isInterrupted()) {
                try {
                    Document doc = Jsoup.connect(mEbook.getmUrl()).get();
                    if (doc != null) {
                        Elements elements = doc.select("div.col-md-8");
                        for (Element element : elements) {

                            Element authorSubject = element.getElementsByTag("h5").first();
                            Element pdfSubject = element.getElementsByClass("btn-danger").first();
                            if (authorSubject != null) {
                                mEbook.setmAuthorName(authorSubject.text());
                            }
                            if (pdfSubject != null) {
                                mEbook.setmPdfLink(pdfSubject.attr("href"));
                            }
                            data.add(mEbook);
                            executors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mCallback.onFinishDownload(data);
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "doInBackground: loi loading");
                    if (mCallback != null) {
                        mCallback.isLastPage(true);
                    }
                }
            }
        }
    }

    private class DownloadEbookDetailTask extends AsyncTask<String, Void, Void> implements Observer {

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
                    for (Element element : elements) {

                        Element authorSubject = element.getElementsByTag("h5").first();
                        Element pdfSubject = element.getElementsByClass("btn-danger").first();
                        if (authorSubject != null) {
                            ebook.setmAuthorName(authorSubject.text());
                        }
                        if (pdfSubject != null) {
                            ebook.setmPdfLink(pdfSubject.attr("href"));
                        }
                        data.add(ebook);
                        if (isCancelled()) {
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: loi loading");
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


        @Override
        public void update(boolean check) {
            this.cancel(check);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled: ");
        }
    }
    

    public interface DownloadEbookCallback {
        void onFinishDownload(List<Ebook> data);
        void isLastPage(boolean bool);
    }

}