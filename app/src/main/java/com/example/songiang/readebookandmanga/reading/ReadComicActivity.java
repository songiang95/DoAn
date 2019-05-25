package com.example.songiang.readebookandmanga.reading;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.MyPagerAdapter;
import com.example.songiang.readebookandmanga.detail.DetailActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadComicActivity extends AppCompatActivity implements ReadingConstract.IView {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ReadingConstract.IPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra(DetailActivity.EXTRA_URL);
        Log.d("affa", "onCreate: url: "+ url);
        mPresenter = new ReadingPresenter();
        mPresenter.attachView(this);
        mPresenter.load(url);

    }


    @Override
    public void showContent(List<String> data) {
        FragmentManager fm = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new MyPagerAdapter(fm, data);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

}
