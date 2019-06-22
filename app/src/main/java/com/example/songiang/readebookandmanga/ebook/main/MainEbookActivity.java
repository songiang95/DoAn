package com.example.songiang.readebookandmanga.ebook.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.EbookAdapter;
import com.example.songiang.readebookandmanga.comic.main.MainActivity;
import com.example.songiang.readebookandmanga.comic.search.SearchActivity;
import com.example.songiang.readebookandmanga.ebook.favorite.FavoriteEbookActivity;
import com.example.songiang.readebookandmanga.ebook.search.SearchEbookActivity;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.example.songiang.readebookandmanga.utils.Utils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainEbookActivity extends AppCompatActivity implements MainConstract.IView {

    public static final String EXTRA_SEARCH_QUERY = "search query";
    private String EBOOK_URL;
    @BindView(R.id.rc_ebook)
    RecyclerView mRcEbook;
    @BindView(R.id.tb_spinner)
    Spinner mSpinner;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private boolean isLoaded;
    private EbookAdapter mAdapter;
    private MainPresenter mPresenter;
    private SlidingRootNav mSlidingBar;
    private int mPageIndex = 1;
    private boolean isLoading = true;
    private LinearLayoutManager linearLayoutManager;
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

        }
    };
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPresenter.reLoad(EBOOK_URL);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ebook);
        ButterKnife.bind(this);
        initNavigation();
        initSpinnerListener();
        initLoadMoreListener();
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRefreshLayout.setProgressViewOffset(true, 0, Utils.dpToPx(80f));
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mPresenter.load(EBOOK_URL);
        acceptPermission();
    }

    private void acceptPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Nếu từ chối cấp quyền, bạn sẽ không thể download")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (edtSearch.getVisibility() == View.GONE)
            mSpinner.setVisibility(View.VISIBLE);

    }

    @Override
    public void showContent(List<Ebook> data) {
        if (!isLoaded) {
            isLoaded = true;
            mAdapter = new EbookAdapter(this, data);
            mRcEbook.setAdapter(mAdapter);
            linearLayoutManager = new LinearLayoutManager(this);
            mRcEbook.setLayoutManager(linearLayoutManager);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private void initSpinnerListener() {
        mSpinner.setDropDownVerticalOffset(100);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ebook_type, R.layout.layout_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        EBOOK_URL = Constant.EBOOK_TIEUTHUYET;
                        mPageIndex = 1;
                        break;
                    case 1:
                        EBOOK_URL = Constant.EBOOK_TAMLY;
                        mPageIndex = 1;
                        break;
                    case 2:
                        EBOOK_URL = Constant.EBOOK_KINHTE;
                        mPageIndex = 1;
                        break;
                    case 3:
                        EBOOK_URL = Constant.EBOOK_MARKETING;
                        mPageIndex = 1;
                        break;
                    case 4:
                        EBOOK_URL = Constant.EBOOK_YHOC;
                        mPageIndex = 1;
                        break;
                    case 5:
                        EBOOK_URL = Constant.EBOOK_NGOAINGU;
                        mPageIndex = 1;
                        break;
                    case 6:
                        EBOOK_URL = Constant.EBOOK_KHOAHOC;
                        mPageIndex = 1;
                        break;
                    case 7:
                        EBOOK_URL = Constant.EBOOK_NGHETHUAT;
                        mPageIndex = 1;
                        break;
                    case 8:
                        EBOOK_URL = Constant.EBOOK_TRINHTHAM;
                        mPageIndex = 1;
                        break;
                    case 9:
                        EBOOK_URL = Constant.EBOOK_TONGIAO;
                        mPageIndex = 1;
                        break;
                    case 10:
                        EBOOK_URL = Constant.EBOOK_TUVI;
                        mPageIndex = 1;
                        break;
                    case 11:
                        EBOOK_URL = Constant.EBOOK_LICHSU;
                        mPageIndex = 1;
                        break;
                    case 12:
                        EBOOK_URL = Constant.EBOOK_VIETNAM;
                        mPageIndex = 1;
                        break;
                    case 13:
                        EBOOK_URL = Constant.EBOOK_KINHDI;
                        mPageIndex = 1;
                        break;
                    case 14:
                        EBOOK_URL = Constant.EBOOK_HUYENBI;
                        mPageIndex = 1;
                        break;
                    case 15:
                        EBOOK_URL = Constant.EBOOK_HOIKY;
                        mPageIndex = 1;
                        break;
                    case 16:
                        EBOOK_URL = Constant.EBOOK_COTICH;
                        mPageIndex = 1;
                        break;
                    case 17:
                        EBOOK_URL = Constant.EBOOK_TRIETHOC;
                        mPageIndex = 1;
                        break;
                    case 18:
                        EBOOK_URL = Constant.EBOOK_KIENTRUC;
                        mPageIndex = 1;
                        break;
                    case 19:
                        EBOOK_URL = Constant.EBOOK_NONGNGHIEP;
                        mPageIndex = 1;
                        break;
                    case 20:
                        EBOOK_URL = Constant.EBOOK_CNTT;
                        mPageIndex = 1;
                        break;
                    case 21:
                        EBOOK_URL = Constant.EBOOK_AMTHUC;
                        mPageIndex = 1;
                        break;
                    case 22:
                        EBOOK_URL = Constant.EBOOK_PHAPLUAT;
                        mPageIndex = 1;
                        break;

                }
                mPresenter.reLoad(EBOOK_URL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                EBOOK_URL = Constant.EBOOK_TIEUTHUYET;

            }
        });
    }

    private void initLoadMoreListener() {
        mRcEbook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (!mPresenter.isLastPage)
                        isLoading = true;
                    else {
                        isLoading = false;
                        if (!recyclerView.canScrollVertically(1))
                            Toast.makeText(getApplication(), "Đã hết eBook", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = false;
                    mPageIndex++;
                    String newUrl = EBOOK_URL + "/" + mPageIndex;
                    mPresenter.load(newUrl);

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (edtSearch.getVisibility() == View.VISIBLE) {
            edtSearch.setVisibility(View.GONE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void initNavigation() {
        mSlidingBar = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.navigation_drawer)
                .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .inject();
    }

    @Override
    public void showProgress() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.toolbar_nav)
    public void onClickNav() {
        if (mSlidingBar.isMenuClosed()) {
            mSlidingBar.openMenu();
        } else {
            mSlidingBar.closeMenu();
        }
    }

    @OnClick(R.id.toolbar_search)
    public void onClickSearch() {
        if (edtSearch.getVisibility() == View.GONE) {
            mSpinner.setVisibility(View.GONE);
            edtSearch.setVisibility(View.VISIBLE);
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
            edtSearch.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.toolbar_favorite)
    public void onClickFavorite() {
        Intent intent = new Intent(this, FavoriteEbookActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.toolbar_change_mode)
    public void onClickChangeMode() {
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Utils.hideKeyboard(this);
        if (edtSearch.getVisibility() == View.VISIBLE) {
            edtSearch.setVisibility(View.GONE);
            mSpinner.setVisibility(View.VISIBLE);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void performSearch() {

        String searchQuery = edtSearch.getText().toString();
        if (!searchQuery.equals("")) {
            Utils.hideKeyboard(this);
            edtSearch.setText("");
            edtSearch.setVisibility(View.GONE);
            Intent intent = new Intent(this, SearchEbookActivity.class);
            intent.putExtra(EXTRA_SEARCH_QUERY, searchQuery);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Bạn cần nhập sách cần tìm!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelDownload();
    }
}
