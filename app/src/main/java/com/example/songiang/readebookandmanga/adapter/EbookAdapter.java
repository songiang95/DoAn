package com.example.songiang.readebookandmanga.adapter;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.ebook.reading.ReadEbookActivity;
import com.example.songiang.readebookandmanga.ebook.reading.ReadEbookOfflineActivity;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.example.songiang.readebookandmanga.utils.Utils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Notification;
import android.app.NotificationManager;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.MyEbookViewHolder> {


    private List<Ebook> data;
    private Context mContext;

    public EbookAdapter(Context context, List<Ebook> data) {
        this.data = data;
        mContext = context;
    }

    @NonNull
    @Override
    public MyEbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.layout_ebook_item, parent, false);
        return new MyEbookViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyEbookViewHolder holder, int position) {
        final Ebook ebook = data.get(position);
        if (ebook != null) {
            Glide.with(mContext)
                    .load(ebook.getCover())
                    .into(holder.ivBookCover);
            holder.tvBookTitle.setText(ebook.getTitle());
            holder.tvAuthor.setText(ebook.getAuthorName());
            if (Utils.isFavorited(ebook)) {
                Glide.with(holder.itemView).load(R.drawable.ic_favorite_red_24dp).into(holder.ivFavorite);
            } else {
                Glide.with(holder.itemView).load(R.drawable.ic_favorite_gray_24dp).into(holder.ivFavorite);
            }
            // holder.tvBookType.setText(ebook.getmBookType());
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyEbookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_book_cover)
        RoundedImageView ivBookCover;
        @BindView(R.id.tv_book_title)
        TextView tvBookTitle;
        @BindView(R.id.tv_book_author)
        TextView tvAuthor;
        @BindView(R.id.iv_favorite)
        ImageView ivFavorite;

        public MyEbookViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btn_read)
        public void onClickRead() {
            final Ebook ebook = data.get(getAdapterPosition());
            if (!Utils.isFavorited(ebook)) {
                Intent intent = new Intent(mContext, ReadEbookActivity.class);
                intent.putExtra(Constant.EXTRA_READ_ONLINE, ebook.getReadOnlineLink());
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, ReadEbookOfflineActivity.class);
                intent.putExtra(Constant.EXTRA_EBOOK, ebook);
                mContext.startActivity(intent);
            }
        }

        private void acceptPermission() {
            TedPermission.with(mContext)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("Nếu từ chối cấp quyền, bạn sẽ không thể download")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check();
        }

        private PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        @OnClick(R.id.iv_favorite)
        public void onClickFavorite() {
            final Ebook ebook = data.get(getAdapterPosition());
            if (!Utils.isFavorited(ebook)) {

                acceptPermission();
                new Thread() {
                    @Override
                    public void run() {
                        Repository repository = new Repository(mContext);
                        repository.getFavoriteEbookDao().insertEbookFavorite(ebook);
                    }
                }.start();
                Hawk.put(ebook.getTitle(), true);
                Glide.with(itemView).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
                final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                final Notification.Builder builder = new Notification.Builder(mContext).setContentTitle("Đang tải xuống...")
                        .setSmallIcon(R.drawable.ic_favorite_red_24dp);
                final int notification_id = (int) System.currentTimeMillis();
                PRDownloader.download(ebook.getPdfLink(), Constant.DOWNLOAD_EBOOK_DIR_PATH, ebook.getTitle() + ".pdf")
                        .build()
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Log.d("abba", "onDownloadComplete: ");
                                builder.setContentTitle("Đã tải xong")
                                        .setContentText("Đã tải xong sách " + ebook.getTitle())
                                        .setProgress(0, 0, false);
                                notificationManager.notify(notification_id, builder.build());
                            }

                            @Override
                            public void onError(Error error) {
                                builder.setContentTitle("Lỗi")
                                        .setContentText("Tải xuống không thành công ")
                                        .setProgress(0, 0, false);
                                notificationManager.notify(notification_id, builder.build());
                            }
                        });
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        Repository repository = new Repository(mContext);
                        repository.getFavoriteEbookDao().deleteFavoriteEbook(ebook);
                        File fileDelete = new File(Constant.DOWNLOAD_EBOOK_DIR_PATH + File.separator + ebook.getTitle() + ".pdf");
                        if (fileDelete.exists()) {
                            fileDelete.delete();
                        }
                    }
                }.start();
                Hawk.put(ebook.getTitle(), false);
                Glide.with(itemView).load(R.drawable.ic_favorite_gray_24dp).into(ivFavorite);
            }
        }
    }

}
