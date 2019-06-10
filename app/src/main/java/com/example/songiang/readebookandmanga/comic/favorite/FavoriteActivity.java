package com.example.songiang.readebookandmanga.comic.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ComicAdapter;
import com.example.songiang.readebookandmanga.comic.detail.DetailActivity;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements ComicAdapter.OnItemClickListener {


    private List<Comic> dataFavorite;
    @BindView(R.id.rc_favorite)
    RecyclerView rcFavorite;
    ComicAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_comic);
        ButterKnife.bind(this);
        rcFavorite.setLayoutManager(new GridLayoutManager(this, 3));


    }

    private class GetFavoriteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Repository repository = new Repository(getApplicationContext());
            dataFavorite = (ArrayList) repository.getFavoriteComicDao().getAllFavoriteComic();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new ComicAdapter(FavoriteActivity.this, dataFavorite, FavoriteActivity.this);
            rcFavorite.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetFavoriteTask().execute();
    }

    @Override
    public void onItemClick(View v, Comic comic) {
        if (comic != null) {
            Intent intentToDetail = new Intent(this, DetailActivity.class);
            intentToDetail.putExtra(Constant.EXTRA_COMIC, comic);
            startActivity(intentToDetail);
        } else {
            Toast.makeText(this, "Comic data error", Toast.LENGTH_LONG).show();
        }
    }
}
