package com.example.songiang.readebookandmanga.detail;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ChapterAdapter;
import com.example.songiang.readebookandmanga.main.MainActivity;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.reading.ReadComicActivity;
import com.makeramen.roundedimageview.RoundedImageView;

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
    private List<String> listChap;
    private DetailContract.IPresenter mPresenter;
    public static final String EXTRA_URL = "url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent receiveIntent = getIntent();
        Comic comic = (Comic) receiveIntent.getSerializableExtra(MainActivity.EXTRA_COMIC);
        mPresenter = new DetailPresenter();
        mPresenter.attachView(this);
        mPresenter.load(comic);
    }


    private void getChapList(Comic comic) {
        Map map = comic.getMap();
        listChap = new ArrayList<String>(comic.getMap().values());
        Collections.reverse(listChap);
    }

    @Override
    public void showContent(Comic comic) {
        getChapList(comic);
        tvContinue.setText("Read 1");
        tvTitle.setText(comic.getName());
        tvAuthor.setText(comic.getArtist());
        Glide.with(this).load(comic.getImage()).into(ivCover);
        ChapterAdapter chapterAdapter = new ChapterAdapter(listChap,this);
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


    @OnClick(R.id.tv_read_continue)
    public void onClickReadContinue()
    { }

    @Override
    public void onItemClick(View v, String url) {
        if (url!=null) {
            Intent intent = new Intent(this, ReadComicActivity.class);
            intent.putExtra(EXTRA_URL,url);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }
}
