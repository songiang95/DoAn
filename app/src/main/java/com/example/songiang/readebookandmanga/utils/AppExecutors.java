package com.example.songiang.readebookandmanga.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AppExecutors {
    private Executor networkIO;
    private Executor mainThread;
    private static AppExecutors INSTANCE;

    private AppExecutors()
    {
        networkIO = new ThreadPoolExecutor(3,3,3000, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
        mainThread = new MainThreadExecutor();
    }

    public static AppExecutors getInstance(){
        if (INSTANCE == null) {
            synchronized (AppExecutors.class)
            {
                if (INSTANCE == null) {
                    INSTANCE = new AppExecutors();
                }
            }

        }
        return INSTANCE;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }


    private class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
