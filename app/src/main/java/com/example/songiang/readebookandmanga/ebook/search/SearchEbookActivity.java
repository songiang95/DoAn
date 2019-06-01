package com.example.songiang.readebookandmanga.ebook.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.EbookAdapter;
import com.example.songiang.readebookandmanga.ebook.main.MainEbookActivity;
import com.example.songiang.readebookandmanga.model.Ebook;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchEbookActivity extends AppCompatActivity implements SearchEbookContract.IView {

    @BindView(R.id.rc_search)
    RecyclerView rcSearch;
    @BindView(R.id.tv_search_query)
    TextView tvSearchQuery;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private SearchEbookContract.IPresenter mPresenter;
    private EbookAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ebook);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        searchQuery = intent.getStringExtra(MainEbookActivity.EXTRA_SEARCH_QUERY);
        mPresenter = new SearchEbookPresenter();
        mPresenter.attachView(this);
        mPresenter.load(searchQuery);

    }

    @Override
    public void showContent(List<Ebook> list) {
        if (list.size() > 0) {
            mAdapter = new EbookAdapter(this, list);
            rcSearch.setAdapter(mAdapter);
            linearLayoutManager = new LinearLayoutManager(this);
            rcSearch.setLayoutManager(linearLayoutManager);
            tvSearchQuery.setText(searchQuery);
        } else {
            tvSearchQuery.setText("Không tìm thấy sách");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelDownload();
    }
}
