package com.example.songiang.readebookandmanga.comic.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ComicAdapter;
import com.example.songiang.readebookandmanga.comic.detail.DetailActivity;
import com.example.songiang.readebookandmanga.comic.main.MainActivity;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchContract.IView, ComicAdapter.OnItemClickListener {

    private String searchUrl = "http://mangak.info/?s=";

    @BindView(R.id.rc_search)
    RecyclerView rcSearch;
    @BindView(R.id.tv_search_query)
    TextView tvSearchQuery;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private SearchPresenter mPresenter;
    private ComicAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isLoading = true;
    private boolean isLoaded = false;
    private int mPageIndex = 1;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
        Intent receiveIntent = getIntent();
        searchQuery = receiveIntent.getStringExtra(MainActivity.EXTRA_SEARCH_QUERY);

        searchUrl = searchUrl + searchQuery;
        mPresenter.load(searchUrl);
        initLoadMoreListener();

    }


    @Override
    public void showContent(List<Comic> list) {
        if (!isLoaded) {
            isLoaded = true;
            mAdapter = new ComicAdapter(this, list, this);
            rcSearch.setAdapter(mAdapter);
            gridLayoutManager = new GridLayoutManager(this, 3);
            rcSearch.setLayoutManager(gridLayoutManager);
            rcSearch.setHasFixedSize(true);
            if (list.size() == 0) {
                tvSearchQuery.setText("Không tìm được truyện");
            } else {
                tvSearchQuery.setText(searchQuery);
            }
        } else {
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

    @Override
    public void onItemClick(View v, Comic comic) {
        if (comic != null) {
            Intent intentToDetail = new Intent(this, DetailActivity.class);
            intentToDetail.putExtra(MainActivity.EXTRA_COMIC, comic);
            intentToDetail.setAction(Constant.PREF_ONLINE);
            startActivity(intentToDetail);
        } else {
            Toast.makeText(this, "Comic data error", Toast.LENGTH_LONG).show();
        }
    }

    private void initLoadMoreListener() {
        rcSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (!mPresenter.isLastPage)
                        isLoading = true;
                    else {
                        isLoading = false;
                        if (!recyclerView.canScrollVertically(1))
                            Toast.makeText(getApplication(), "No more comic", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = false;
                    mPageIndex++;
                    String pageIndex = "http://mangak.info/page/" + mPageIndex + "/?s=";
                    String searchUrl = pageIndex + searchQuery;
                    mPresenter.load(searchUrl);

                }
            }
        });
    }
}
