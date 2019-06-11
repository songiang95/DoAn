package com.example.songiang.readebookandmanga.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.orhanobut.hawk.Hawk;

import java.io.File;

public class Utils {


    public static boolean isFavorited(Comic comic) {
        boolean bool = Hawk.get(comic.getName(), false);
        return bool;
    }

    public static boolean isFavorited(Ebook ebook) {
        boolean bool = Hawk.get(ebook.getTitle(), false);
        return bool;
    }

    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void showKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void writeToDisk(Context context, @NonNull String imageUrl, @NonNull String downloadSubfolder, int chapNumb, int picNumb) {
        Uri imageUri = Uri.parse(imageUrl);
        String fileName = chapNumb + "_" + picNumb;
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(imageUri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription(imageUrl);
        request.allowScanningByMediaScanner();
        request.setDestinationUri(getDownloadDestination(downloadSubfolder));
        request.setTitle(fileName);
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory() + File.separator + "ComicDownload" + File.separator + downloadSubfolder,fileName+".jpg");
        downloadManager.enqueue(request);

    }

    @NonNull
    private static Uri getDownloadDestination(String downloadSubpath) {
        File destinationFile = new File(Environment.getExternalStorageDirectory() + File.separator + "ComicDownload" + File.separator + downloadSubpath);
        boolean success;
        if (!destinationFile.exists()) {
            success = destinationFile.mkdirs();
        }
        return Uri.fromFile(destinationFile);
    }
}
