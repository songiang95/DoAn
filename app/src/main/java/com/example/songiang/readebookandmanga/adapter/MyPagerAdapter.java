package com.example.songiang.readebookandmanga.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.songiang.readebookandmanga.comic.reading.ViewImageFragment;

import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> data;
    public MyPagerAdapter(FragmentManager fm, List<String> imgsUrl) {

        super(fm);
        this.data = imgsUrl;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ViewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL", data.get(i));
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int getCount() {
        return data.size();
    }
}

