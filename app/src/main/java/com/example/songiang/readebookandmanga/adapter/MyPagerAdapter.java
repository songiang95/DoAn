package com.example.songiang.readebookandmanga.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.songiang.readebookandmanga.comic.reading.ViewImageFragment;

import java.io.File;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> data;
    public static final String EXTRA_IMAGE_NAME = "image_name";
    public static final String EXTRA_STATE = "state";
    private boolean isOnline;

    public MyPagerAdapter(FragmentManager fm, List<String> imgsUrl, boolean isOnline) {
        super(fm);
        this.data = imgsUrl;
        this.isOnline = isOnline;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ViewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_IMAGE_NAME, data.get(i));
        bundle.putBoolean(EXTRA_STATE, isOnline);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}

