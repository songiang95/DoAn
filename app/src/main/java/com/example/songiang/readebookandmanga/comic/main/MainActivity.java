package com.example.songiang.readebookandmanga.comic.main;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.songiang.readebookandmanga.comic.favorite.FavoriteActivity;
import com.example.songiang.readebookandmanga.comic.search.SearchActivity;
import com.example.songiang.readebookandmanga.ebook.favorite.FavoriteEbookActivity;
import com.example.songiang.readebookandmanga.ebook.main.MainEbookActivity;
import com.example.songiang.readebookandmanga.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.comic.View.BaseActivity;
import com.example.songiang.readebookandmanga.adapter.ComicAdapter;
import com.example.songiang.readebookandmanga.comic.detail.DetailActivity;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.orhanobut.hawk.Hawk;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.IView, ComicAdapter.OnItemClickListener {

    public static final String EXTRA_COMIC = "comic";
    public static final String EXTRA_SEARCH_QUERY = "search";

    @BindView(R.id.recycle_manga_list)
    RecyclerView mRecycleView;
    private String MANGA_URL;
    @BindView(R.id.tb_spinner)
    Spinner mSpinner;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.fr_search_box)
    FrameLayout mFrSearchBox;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private SlidingRootNav mSlidingBar;
    private MainContract.IPresenter mPresenter;
    private ComicAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isLoading = true;
    private int mPageIndex = 1;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic);
        initNavigation();
        ButterKnife.bind(this);

        Fresco.initialize(this);

        //Activate toolbar
        if (!Hawk.isBuilt()) {
            Hawk.init(this).build();
        }
        activateToolbar();

        initSpinnerListener();
        initLoadMoreListener();
        mRefreshLayout.setProgressViewOffset(true, 0, Utils.dpToPx(80f));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.reLoad(MANGA_URL);
            }
        });
        mPresenter = new Presenter();
        mPresenter.attachView(this);
        mPresenter.load(MANGA_URL);

    }

    @Override
    protected void onResume() {
        mSpinner.setVisibility(View.VISIBLE);
        super.onResume();
    }

    private void initLoadMoreListener() {
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
                    mPresenter.load(MANGA_URL + pageIndex);

                }
            }
        });
    }

    private void initSpinnerListener() {
        mSpinner.setDropDownVerticalOffset(100);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.comic_type, R.layout.layout_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        MANGA_URL = Constant.MANGAK_NEW;
                        mPageIndex = 1;
                        break;
                    case 1:
                        MANGA_URL = Constant.MANGAK_ACTION;
                        mPageIndex = 1;
                        break;
                    case 2:
                        MANGA_URL = Constant.MANGAK_COMEDY;
                        mPageIndex = 1;
                        break;
                    case 3:
                        MANGA_URL = Constant.MANGAK_HORROR;
                        mPageIndex = 1;
                        break;
                    case 4:
                        MANGA_URL = Constant.MANGAK_MYSTERY;
                        mPageIndex = 1;
                        break;
                    case 5:
                        MANGA_URL = Constant.MANGAK_SCHOOL_LIFE;
                        mPageIndex = 1;
                        break;
                    case 6:
                        MANGA_URL = Constant.MANGAK_ADVENTURE;
                        mPageIndex = 1;
                        break;
                    case 7:
                        MANGA_URL = Constant.MANGAK_SEINEN;
                        mPageIndex = 1;
                        break;
                    case 8:
                        MANGA_URL = Constant.MANGAK_SHOUNEN;
                        mPresenter.reLoad(MANGA_URL);
                        mPageIndex = 1;
                        break;
                    case 9:
                        MANGA_URL = Constant.MANGAK_SCIFI;
                        mPageIndex = 1;
                        break;
                    case 10:
                        MANGA_URL = Constant.MANGAK_PSYCHOLOGICAL;
                        mPageIndex = 1;
                        break;
                    default:
                        MANGA_URL = Constant.MANGAK_NEW;
                        mPageIndex = 1;
                        break;

                }
                mPresenter.reLoad(MANGA_URL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MANGA_URL = Constant.MANGAK_NEW;

            }
        });
    }

    private void initNavigation() {
        mSlidingBar = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.navigation_drawer)
                .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.5f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .inject();
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
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToastLastPage() {
        Toast.makeText(this, "Bạn đã ở cuối trang!", Toast.LENGTH_SHORT).show();
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
        if (mSlidingBar.isMenuClosed()) {
            mSlidingBar.openMenu();
        } else {
            mSlidingBar.closeMenu();
        }
    }

    @OnClick(R.id.toolbar_home)
    public void onClickHome() {
        Toast.makeText(this, "Bạn đang ở màn hình chính", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.toolbar_favorite)
    public void onClickFavorite() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.toolbar_search)
    public void onClickSearch() {
        if (mFrSearchBox.getVisibility() == View.GONE) {
            mSpinner.setVisibility(View.GONE);
            mFrSearchBox.setVisibility(View.VISIBLE);
            Utils.showKeyboard(this);
            edtSearch.requestFocus();
            edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        performSearch();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mSpinner.setVisibility(View.VISIBLE);
            mFrSearchBox.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toolbar_change_mode)
    public void onClickChangeMode() {
        startActivity(new Intent(this, MainEbookActivity.class));
    }

    @OnClick(R.id.ll_read_ebook_offline)
    public void onClickReadEbookOffline() {
        Intent intent = new Intent(this, FavoriteEbookActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (mFrSearchBox.getVisibility() == View.VISIBLE) {
            mFrSearchBox.setVisibility(View.GONE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    private void performSearch() {

        String searchQuery = edtSearch.getText().toString();
        if (!searchQuery.equals("")) {
            Utils.hideKeyboard(this);
            edtSearch.setText("");
            mFrSearchBox.setVisibility(View.GONE);
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(EXTRA_SEARCH_QUERY, searchQuery);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Bạn cần nhập truyện cần tìm!", Toast.LENGTH_LONG).show();
        }
    }
}
