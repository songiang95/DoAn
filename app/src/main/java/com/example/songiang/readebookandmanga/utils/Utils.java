package com.example.songiang.readebookandmanga.utils;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.orhanobut.hawk.Hawk;

public class Utils {


    public static boolean isFavorited(Comic comic) {
        boolean bool = Hawk.get(comic.getName(), false);
        return bool;
    }

    public static boolean isFavorited(Ebook ebook) {
        boolean bool = Hawk.get(ebook.getTitle(), false);
        return bool;
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
}
