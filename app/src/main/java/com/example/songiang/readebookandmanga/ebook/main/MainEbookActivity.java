package com.example.songiang.readebookandmanga.ebook.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.EbookAdapter;
import com.example.songiang.readebookandmanga.model.Ebook;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainEbookActivity extends AppCompatActivity implements MainConstract.IView {

    @BindView(R.id.rc_ebook)
    RecyclerView mRcEbook;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private boolean isLoaded;
    private EbookAdapter mAdapter;
    private MainPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ebook);
        ButterKnife.bind(this);
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mPresenter.load("https://sachvui.com/the-loai/tam-ly-ky-nang-song.html");
    }

    @Override
    public void showContent(List<Ebook> data) {
        if (!isLoaded) {
            isLoaded = true;
            mAdapter = new EbookAdapter(this, data);
            mRcEbook.setAdapter(mAdapter);
            mRcEbook.setLayoutManager(new LinearLayoutManager(this));
        }
        else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgress() {
     pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
    pbLoading.setVisibility(View.GONE);
    }
}
