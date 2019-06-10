package com.example.songiang.readebookandmanga.ebook.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ComicAdapter;
import com.example.songiang.readebookandmanga.adapter.EbookAdapter;
import com.example.songiang.readebookandmanga.comic.favorite.FavoriteActivity;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.model.Ebook;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteEbookActivity extends AppCompatActivity {

    @BindView(R.id.rc_favorite)
    RecyclerView rcFavorite;
    private List<Ebook> dataFavorite;
    private EbookAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_ebook);
        ButterKnife.bind(this);
        rcFavorite.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetFavoriteTask().execute();
    }

    private class GetFavoriteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Repository repository = new Repository(getApplicationContext());
            dataFavorite = (ArrayList) repository.getFavoriteEbookDao().getAllFavoriteEbook();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new EbookAdapter(FavoriteEbookActivity.this, dataFavorite);
            rcFavorite.setAdapter(mAdapter);
        }
    }
}
