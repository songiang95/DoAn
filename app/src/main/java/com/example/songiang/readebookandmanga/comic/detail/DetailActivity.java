package com.example.songiang.readebookandmanga.comic.detail;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.ChapterAdapter;
import com.example.songiang.readebookandmanga.adapter.ComicDownloadedAdapter;
import com.example.songiang.readebookandmanga.comic.favorite.FavoriteActivity;
import com.example.songiang.readebookandmanga.comic.main.MainActivity;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.ebook.main.MainEbookActivity;
import com.example.songiang.readebookandmanga.model.Chapter;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.comic.reading.ReadComicActivity;
import com.example.songiang.readebookandmanga.model.ComicDownloaded;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.example.songiang.readebookandmanga.utils.DownloadImageTask;
import com.example.songiang.readebookandmanga.utils.Utils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements DetailContract.IView {

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.iv_detail_cover)
    RoundedImageView ivCover;
    @BindView(R.id.tv_comic_title)
    TextView tvTitle;
    @BindView(R.id.recycle_chap_list)
    RecyclerView mRecycleView;
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
    public static final String EXTRA_CHAPTER_NUMB = "chapter_numb";
    public static final String EXTRA_COMIC_NAME = "comic_name";
    private Comic mComic;
    private ChapterAdapter mChapterAdapter;
    private NotificationManager mNotificationManager;
    private Repository mRepository;
    private ComicDownloaded mComicDownloaded;
    private ChapterAdapter.OnItemClickListener mOnlineCallback = new ChapterAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, String url, int position) {
            if (url != null) {
                Hawk.put(Constant.PREF_CONTINUE_CHAP_NUMB + mComic.getName(), position + 1);
                Hawk.put(Constant.PREF_CONTINUE_CHAP_URL + mComic.getName(), url);

                Intent intent = new Intent(DetailActivity.this, ReadComicActivity.class);
                intent.putExtra(EXTRA_URL, url);
                intent.setAction(Constant.PREF_ONLINE);
                startActivity(intent);
            } else {
                Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    };

    private ChapterAdapter.OnItemClickListener mOfflineCallback = new ChapterAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, String title, int chapterNumb) {
            Hawk.put(Constant.PREF_CONTINUE_CHAP_NUMB + mComicDownloaded.getTitle(), chapterNumb);
            Intent intent = new Intent(DetailActivity.this, ReadComicActivity.class);
            intent.setAction(Constant.PREF_OFFLINE);
            intent.putExtra(DetailActivity.EXTRA_CHAPTER_NUMB, chapterNumb);
            intent.putExtra(DetailActivity.EXTRA_COMIC_NAME, title);
            startActivity(intent);
        }
    };

    private Comparator<Chapter> mChapterComparator = new Comparator<Chapter>() {
        @Override
        public int compare(Chapter o1, Chapter o2) {
            return o1.getChapterNumb() > o2.getChapterNumb() ? 1 : -1;
        }
    };

    private int STORAGE_PERMISSION_CODE = 23;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent receiveIntent = getIntent();
        String action = receiveIntent.getAction();
        mRepository = new Repository(this);
        if (TextUtils.equals(action, Constant.PREF_ONLINE)) {
            llComicInfo.setVisibility(View.VISIBLE);
            mComic = (Comic) receiveIntent.getSerializableExtra(MainActivity.EXTRA_COMIC);
            mPresenter = new DetailPresenter();
            mPresenter.attachView(this);
            mPresenter.load(mComic);
        } else if (TextUtils.equals(action, Constant.PREF_OFFLINE)) {
            llComicInfo.setVisibility(View.GONE);
            mComicDownloaded = (ComicDownloaded) receiveIntent.getSerializableExtra(ComicDownloadedAdapter.EXTRA_COMIC_DOWNLOADED);
            initDownloadedComicInfo(mComicDownloaded);
            initDownloadedChapter(mComicDownloaded);
        }

    }

    private void initDownloadedComicInfo(ComicDownloaded comicDownloaded) {
        tvAuthor.setText(comicDownloaded.getAuthor());
        tvTitle.setText(comicDownloaded.getTitle());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDownloadedChapter(ComicDownloaded comicDownloaded) {
        new AsyncTaskLoadChapter().execute(comicDownloaded);
    }

    class AsyncTaskLoadChapter extends AsyncTask<ComicDownloaded, Void, List<Chapter>> {

        @Override
        protected List<Chapter> doInBackground(ComicDownloaded... comicDownloadeds) {
            ArrayList<Chapter> chapters = (ArrayList) mRepository.getAllChapters(comicDownloadeds[0].getTitle());
            return chapters;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(List<Chapter> chapters) {
            super.onPostExecute(chapters);
            if (chapters != null) {
                chapters.sort(mChapterComparator);
                mComicDownloaded.setChaptersDownloaded(chapters);
                mChapterAdapter = new ChapterAdapter(mComicDownloaded.getChaptersDownloaded(), mComicDownloaded);
                mChapterAdapter.setCallback(mOfflineCallback);
                mRecycleView.setAdapter(mChapterAdapter);
                mRecycleView.hasFixedSize();
                mRecycleView.setLayoutManager(new GridLayoutManager(DetailActivity.this, 5));
            } else {
                Log.d("abba", "chapter is null: ");
            }
        }
    }

    @Override
    protected void onResume() {
        if (mChapterAdapter != null) {
            mChapterAdapter.notifyDataSetChanged();
        }

        if (mComic != null) {
            int chapNumbContinue = Hawk.get(Constant.PREF_CONTINUE_CHAP_NUMB + mComic.getName(), 1);
            tvContinue.setText("Read " + chapNumbContinue);
            if (Utils.isFavorited(mComic)) {
                Glide.with(this).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
            }
        }
        super.onResume();
    }

    private void getChapList(Comic comic) {
        Map map = comic.getMap();
        listChap = new ArrayList<String>(map.values());
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
        mChapterAdapter = new ChapterAdapter(listChap, comic);
        mChapterAdapter.setCallback(mOnlineCallback);
        mRecycleView.setAdapter(mChapterAdapter);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 5));
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
    public void onClickContinue() {
        Intent intent = new Intent(this, ReadComicActivity.class);
        String url = Hawk.get(Constant.PREF_CONTINUE_CHAP_URL + mComic.getName(), listChap.get(0));
        intent.setAction(Constant.PREF_ONLINE);
        intent.putExtra(EXTRA_URL, url);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mTvDownload.getVisibility() == View.VISIBLE) {
            mTvDownload.setVisibility(View.GONE);
            mBotToolbar.setVisibility(View.VISIBLE);
            llComicInfo.setVisibility(View.VISIBLE);
            if (mChapterAdapter != null) {
                mChapterAdapter.setItemSelection(false);
                mChapterAdapter.notifyDataSetChanged();
                mChapterAdapter.clearSelectedItem();
            }
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.iv_download)
    public void onClickSelectToDownload() {
        mComicDownloaded = new ComicDownloaded();
        mComicDownloaded.setTitle(mComic.getName());
        mComicDownloaded.setAuthor(mComic.getArtist());
        mBotToolbar.setVisibility(View.GONE);
        llComicInfo.setVisibility(View.GONE);
        mTvDownload.setVisibility(View.VISIBLE);
        mChapterAdapter.setItemSelection(true);
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Nếu từ chối cấp quyền, bạn sẽ không thể download")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
        PRDownloader.initialize(getApplicationContext());
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

        }
    };
    private DownloadImageTask.CallBack mCallback = new DownloadImageTask.CallBack() {
        @Override
        public void onDownloadFinish(final List<String> data, final int chapterNumb) {
            final Chapter chapter = new Chapter(mComic.getName(), chapterNumb);
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            final NotificationCompat.Builder mNotification = new NotificationCompat.Builder(DetailActivity.this, "M_CH_ID")
                    .setContentTitle("Đã tải xong")
                    .setContentText("Đã tải xong truyện " + mComic.getName() + " tập " + chapterNumb)
                    .setSmallIcon(R.drawable.ic_favorite_red_24dp);
            final int notification_id = (int) System.currentTimeMillis();
            int i = 1;
            for (final String url : data) {
                final int progress = i++;
                PRDownloader.download(url, Constant.DOWNLOAD_COMIC_DIR_PATH + mComic.getName(), chapterNumb + "_" + data.indexOf(url) + ".jpg")
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {
                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {
                                Toast.makeText(DetailActivity.this, "Tạm dừng tải xuống", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                Toast.makeText(DetailActivity.this, "Hủy tải xuống", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                                if (progress == data.size()) {
                                    if (!Hawk.get(Constant.PREF_DOWNLOADED, false)) {
                                        Hawk.put(Constant.PREF_DOWNLOADED + mComic.getName(), true);
                                        downloadCover();
                                        mRepository.insertDownloadedComic(mComicDownloaded);
                                    }
                                    mRepository.insertChapter(chapter);
                                    mNotificationManager.notify(notification_id, mNotification.build());
                                }

                            }

                            @Override
                            public void onError(Error error) {

                                mNotification.setContentTitle("Lỗi")
                                        .setContentText("Tải xuống không thành công");
                                mNotificationManager.notify(notification_id, mNotification.build());
                            }
                        });
            }
        }
    };

    @OnClick(R.id.tv_download)
    public void onClickStartDownload() {
        Map<Integer, String> selectedList = mChapterAdapter.getSelectedItem();
        if (selectedList.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn tập truyện để tải xuống", Toast.LENGTH_SHORT).show();
        } else {
            for (Integer key : selectedList.keySet()) {
                DownloadImageTask downloadImageTask = new DownloadImageTask(key);
                downloadImageTask.setCallback(mCallback);
                downloadImageTask.execute(selectedList.get(key));
            }
        }
    }

    public void downloadCover() {
        PRDownloader.download(mComic.getImage(), Constant.DOWNLOAD_COMIC_DIR_PATH + mComic.getName(), "cover_" + mComic.getName() + ".jpg")
                .build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d("abba", "onDownloadComplete: download cover success");
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
    }

//    @Override
//    public void onDownloadFinish(List<String> data, int chapterNumb) {
//        int i = 1;
//        for (String url : data) {
//            Utils.writeToDisk(this, url, mComic.getName(), chapterNumb, i);
//            i++;
//
//        }
//    }

//    private boolean isReadStorageAllowed() {
//        //Getting the permission status
//        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        //If permission is granted returning true
//        if (result == PackageManager.PERMISSION_GRANTED)
//            return true;
//        else {
//            requestStoragePermission();
//            return true;
//        }
//        //If permission is not granted returning false
//
//    }

//    private void requestStoragePermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }


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
        onBackPressed();
    }


    @OnClick(R.id.iv_favorite)
    public void onClickFavorite() {
        if (mComic != null) {
            if (!Utils.isFavorited(mComic)) {
                new Thread() {
                    @Override
                    public void run() {
                        Repository repository = new Repository(getApplicationContext());
                        repository.getFavoriteComicDao().insertComicFavorite(mComic);
                    }
                }.start();
                Hawk.put(mComic.getName(), true);
                Glide.with(this).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        Repository repository = new Repository(getApplicationContext());
                        repository.getFavoriteComicDao().deleteFavoriteComic(mComic);
                    }
                }.start();
                Hawk.put(mComic.getName(), false);
                Glide.with(this).load(R.drawable.ic_favorite_black_24dp).into(ivFavorite);
            }
        }
    }


}
