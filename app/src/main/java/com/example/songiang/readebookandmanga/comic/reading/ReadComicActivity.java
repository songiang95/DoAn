package com.example.songiang.readebookandmanga.comic.reading;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.MyPagerAdapter;
import com.example.songiang.readebookandmanga.comic.detail.DetailActivity;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadComicActivity extends AppCompatActivity implements ReadingConstract.IView {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ReadingConstract.IPresenter mPresenter;
    private PagerAdapter mPagerAdapter;
    private List<String> mListImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (TextUtils.equals(action, Constant.PREF_ONLINE)) {
            String url = intent.getStringExtra(DetailActivity.EXTRA_URL);
            mPresenter = new ReadingPresenter();
            mPresenter.attachView(this);
            mPresenter.load(url);
        } else if (TextUtils.equals(action, Constant.PREF_OFFLINE)) {
            mListImage = new ArrayList<>();
            String comicName = intent.getStringExtra(DetailActivity.EXTRA_COMIC_NAME);
            int chapterNumb = intent.getIntExtra(DetailActivity.EXTRA_CHAPTER_NUMB, 0);
            String fileName = "";
            int i = 0;
            boolean isFileExits = false;
            do {
                fileName = chapterNumb + "_" + i + ".jpg";
                File file = new File(Constant.DOWNLOAD_COMIC_DIR_PATH + comicName + File.separator + fileName);
                if (file.exists()) {
                    Log.d("abba", "image exists: " + file.getName());
                    mListImage.add(file.getAbsolutePath());
                    isFileExits = true;
                    i++;
                } else {
                    break;
                }
            } while (isFileExits);
            readComicOffline();
        }
    }

    private void readComicOffline() {
        FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new MyPagerAdapter(fm, mListImage, false);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void showContent(List<String> data) {
        FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new MyPagerAdapter(fm, data, true);
        viewPager.setAdapter(mPagerAdapter);
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
