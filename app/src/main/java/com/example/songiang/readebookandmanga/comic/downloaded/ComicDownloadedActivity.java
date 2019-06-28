package com.example.songiang.readebookandmanga.comic.downloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ComicDownloadedAdapter;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.model.ComicDownloaded;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDownloadedActivity extends AppCompatActivity {

    @BindView(R.id.rc_downloaded)
    RecyclerView mRecycleView;
    private Repository mRepository;
    private List<ComicDownloaded> mListComic;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_downloaded);
        ButterKnife.bind(this);
        mRepository = new Repository(this);
        new MyAsyntask().execute();

    }

    class MyAsyntask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mListComic = mRepository.getAllComicDownloaded();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ComicDownloadedAdapter adapter = new ComicDownloadedAdapter(mListComic, ComicDownloadedActivity.this);
            mRecycleView.setAdapter(adapter);
            mRecycleView.setLayoutManager(new GridLayoutManager(ComicDownloadedActivity.this, 3));
            mRecycleView.hasFixedSize();
        }
    }
}
