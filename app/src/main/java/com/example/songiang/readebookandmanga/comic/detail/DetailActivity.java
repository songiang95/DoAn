package com.example.songiang.readebookandmanga.comic.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ChapterAdapter;
import com.example.songiang.readebookandmanga.comic.favorite.FavoriteActivity;
import com.example.songiang.readebookandmanga.comic.main.MainActivity;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.ebook.main.MainEbookActivity;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.comic.reading.ReadComicActivity;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.example.songiang.readebookandmanga.utils.DownloadImageTask;
import com.example.songiang.readebookandmanga.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements DetailContract.IView, ChapterAdapter.OnItemClickListener, DownloadImageTask.CallBack {

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.iv_detail_cover)
    RoundedImageView ivCover;
    @BindView(R.id.tv_comic_title)
    TextView tvTitle;
    @BindView(R.id.recycle_chap_list)
    RecyclerView recyclerView;
    @BindView(R.id.tv_read_continue)
    TextView tvContinue;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.ll_top)
    LinearLayout llComicInfo;
    @BindView(R.id.bottom_toolbar)
    LinearLayout mBotToolbar;
    @BindView(R.id.tv_download)
    TextView mTvDownload;
    private List<String> listChap;
    private DetailContract.IPresenter mPresenter;
    public static final String EXTRA_URL = "url";
    private Comic comic;
    private ChapterAdapter chapterAdapter;
//    private final AltexImageDownloader downloader = new AltexImageDownloader(new AltexImageDownloader.OnImageLoaderListener() {
//        @Override
//        public void onError(AltexImageDownloader.ImageError error) {
//            Log.d("abba", "onError: ");
//        }
//
//        @Override
//        public void onProgressChange(int percent) {
//        }
//
//        @Override
//        public void onComplete(Bitmap result) {
//            Log.d("abba", "onComplete: ");
//        }
//    });

    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent receiveIntent = getIntent();
        comic = (Comic) receiveIntent.getSerializableExtra(MainActivity.EXTRA_COMIC);
        mPresenter = new DetailPresenter();
        mPresenter.attachView(this);
        mPresenter.load(comic);

    }


    @Override
    protected void onResume() {
        super.onResume();
        int chapNumbContinue = Hawk.get(Constant.PREF_CONTINUE_CHAP_NUMB + comic.getName(), 1);
        tvContinue.setText("Read " + chapNumbContinue);
        if (Utils.isFavorited(comic)) {
            Glide.with(this).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
        }
    }

    private void getChapList(Comic comic) {
        Map map = comic.getMap();
        listChap = new ArrayList<String>(comic.getMap().values());
        Collections.reverse(listChap);
    }

    @Override
    public void showContent(Comic comic) {
        getChapList(comic);
        tvTitle.setText(comic.getName());
        tvAuthor.setText(comic.getArtist());
        Glide.with(this)
                .load(comic.getImage())
                .centerCrop()
                .into(ivCover);
        chapterAdapter = new ChapterAdapter(listChap, this);
        recyclerView.setAdapter(chapterAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
    public void onItemClick(View v, String url, int position) {
        if (url != null) {

            Hawk.put(Constant.PREF_CONTINUE_CHAP_NUMB + comic.getName(), position + 1);
            Hawk.put(Constant.PREF_CONTINUE_CHAP_URL + comic.getName(), url);

            Intent intent = new Intent(this, ReadComicActivity.class);
            intent.putExtra(EXTRA_URL, url);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.tv_read_continue)
    public void onClickContinue() {
        Intent intent = new Intent(this, ReadComicActivity.class);
        String url = Hawk.get(Constant.PREF_CONTINUE_CHAP_URL + comic.getName(), listChap.get(0));
        intent.putExtra(EXTRA_URL, url);
        startActivity(intent);
    }

    @OnClick(R.id.iv_download)
    public void onClickSelectToDownload() {
        mBotToolbar.setVisibility(View.GONE);
        llComicInfo.setVisibility(View.GONE);
        mTvDownload.setVisibility(View.VISIBLE);
        chapterAdapter.setItemSelection(true);
    }

    @OnClick(R.id.tv_download)
    public void onClickStartDownload() {
        Log.d("abba", "onClickStartDownload: ");
        if (isReadStorageAllowed()) {
            Map<Integer, String> selectedList = chapterAdapter.getSelectedItem();
            for (Integer key : selectedList.keySet()) {
                new DownloadImageTask(key, this).execute(selectedList.get(key));
            }
        }
    }

    @Override
    public void onDownloadFinish(List<String> data, int chapterNumb) {
        int i = 1;
        for (String url : data) {
            Utils.writeToDisk(this,url,comic.getName(),chapterNumb,i);
            i++;

        }
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        else {
            requestStoragePermission();
            return true;
        }
        //If permission is not granted returning false

    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @OnClick(R.id.toolbar_change_mode)
    public void onClickChangeMode() {
        Intent intent = new Intent(this, MainEbookActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.toolbar_favorite)
    public void onClickToolbarFavorite() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.toolbar_home)
    public void onClickHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.iv_favorite)
    public void onClickFavorite() {
        if (!Utils.isFavorited(comic)) {
            new Thread() {
                @Override
                public void run() {
                    Repository repository = new Repository(getApplicationContext());
                    repository.getFavoriteComicDao().insertComicFavorite(comic);
                }
            }.start();
            Hawk.put(comic.getName(), true);
            Glide.with(this).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
        } else {
            new Thread() {
                @Override
                public void run() {
                    Repository repository = new Repository(getApplicationContext());
                    repository.getFavoriteComicDao().deleteFavoriteComic(comic);
                }
            }.start();
            Hawk.put(comic.getName(), false);
            Glide.with(this).load(R.drawable.ic_favorite_black_24dp).into(ivFavorite);
        }
    }


}
