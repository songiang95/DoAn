package com.example.songiang.readebookandmanga.main;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.View.BaseActivity;
import com.example.songiang.readebookandmanga.adapter.ComicAdapter;
import com.example.songiang.readebookandmanga.detail.DetailActivity;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.IView, ComicAdapter.OnItemClickListener {

    public static final String EXTRA_COMIC = "comic";

    @BindView(R.id.toolbar_top)
    AppBarLayout toolbarTop;
    @BindView(R.id.recycle_manga_list)
    RecyclerView mRecycleView;
    private String MANGA_URL;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tb_spinner)
    Spinner mSpinner;

    private SlidingRootNavBuilder mSlidingBar;
    private MainContract.IPresenter mPresenter;
    private ComicAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isLoading = true;
    private int mPageIndex = 1;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //Activate toolbar
        activateToolbar();
        initNavigation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarTop.setOutlineProvider(null);
        }

        initSpinnerListener();
        initLoadmorListner();
        mPresenter = new Presenter();
        mPresenter.attachView(this);
        mPresenter.load(MANGA_URL);


    }


    private void initLoadmorListner() {
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (!mPresenter.isLastPage())
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
                    String pageIndex = "page/" + mPageIndex + "/";
                    mPresenter.loadMore(MANGA_URL + pageIndex);

                }
            }
        });
    }

    private void initSpinnerListener() {
        mSpinner.setDropDownVerticalOffset(100);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        MANGA_URL = Constant.MANGAK_NEW;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 1:
                        MANGA_URL = Constant.MANGAK_ACTION;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 2:
                        MANGA_URL = Constant.MANGAK_COMEDY;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 3:
                        MANGA_URL = Constant.MANGAK_HORROR;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 4:
                        MANGA_URL = Constant.MANGAK_MYSTERY;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 5:
                        MANGA_URL = Constant.MANGAK_SCHOOL_LIFE;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 6:
                        MANGA_URL = Constant.MANGAK_ADVENTURE;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 7:
                        MANGA_URL = Constant.MANGAK_SEINEN;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 8:
                        MANGA_URL = Constant.MANGAK_SHOUNEN;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 9:
                        MANGA_URL = Constant.MANGAK_SCIFI;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 10:
                        MANGA_URL = Constant.MANGAK_PSYCHOLOGICAL;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    default:
                        MANGA_URL = Constant.MANGAK_NEW;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MANGA_URL = Constant.MANGAK_NEW;

            }
        });
    }

    private void initNavigation() {
        new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.navigation_drawer)
                .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .inject();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("affa", "onDestroy: main ");
    }


    @Override
    public void showContent(List<Comic> listData) {
        if (!isLoaded) {
            isLoaded = true;
            mAdapter = new ComicAdapter(this, listData, this);
            mRecycleView.setAdapter(mAdapter);
            gridLayoutManager = new GridLayoutManager(this, 3);
            mRecycleView.setLayoutManager(gridLayoutManager);
            mRecycleView.setHasFixedSize(true);

        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideContent() {

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
            intentToDetail.putExtra(EXTRA_COMIC, comic);
            startActivity(intentToDetail);
        } else {
            Toast.makeText(this, "Comic data error", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.toolbar_nav)
    public void onClickNav() {
    }

}
