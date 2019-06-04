package com.example.songiang.readebookandmanga.comic.detail;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ChapterAdapter;
import com.example.songiang.readebookandmanga.comic.main.MainActivity;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.comic.reading.ReadComicActivity;
import com.example.songiang.readebookandmanga.utils.Constant;
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

public class DetailActivity extends AppCompatActivity implements DetailContract.IView, ChapterAdapter.OnItemClickListener {

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
    private List<String> listChap;
    private DetailContract.IPresenter mPresenter;
    public static final String EXTRA_URL = "url";
    private Comic comic;

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
        if(Utils.isFavorited(comic)){
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
        Glide.with(this).load(comic.getImage()).into(ivCover);
        ChapterAdapter chapterAdapter = new ChapterAdapter(listChap, this);
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

    @OnClick(R.id.iv_favorite)
    public void onClickFavorite()
    {
        if (!Utils.isFavorited(comic)) {
            new Thread() {
                @Override
                public void run() {
                    Repository repository = new Repository(getApplicationContext());
                    repository.getFavoriteDAO().insertComicFavorite(comic);
                }
            }.start();
            Hawk.put(comic.getName(),true);
            Glide.with(this).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
        }
        else{
            new Thread() {
                @Override
                public void run() {
                    Repository repository = new Repository(getApplicationContext());
                    repository.getFavoriteDAO().deleteFavoriteGrammar(comic);
                }
            }.start();
            Hawk.put(comic.getName(),false);
            Glide.with(this).load(R.drawable.ic_favorite_black_24dp).into(ivFavorite);
        }
    }
}
