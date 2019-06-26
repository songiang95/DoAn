package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.comic.detail.DetailActivity;
import com.example.songiang.readebookandmanga.model.ComicDownloaded;
import com.example.songiang.readebookandmanga.utils.Constant;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDownloadedAdapter extends RecyclerView.Adapter<ComicDownloadedAdapter.MyViewHolder> {

    public static final String EXTRA_COMIC_DOWNLOADED = "comic_downloaded";
    private List<ComicDownloaded> mListComic;
    private Context mContext;


    public ComicDownloadedAdapter(List<ComicDownloaded> list, Context context) {
        mListComic = list;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_comic_downloaded, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ComicDownloaded comic = mListComic.get(position);
        File file = new File(Constant.DOWNLOAD_COMIC_DIR_PATH + comic.getTitle() + File.separator + "cover_" + comic.getTitle() + ".jpg");
        Glide.with(mContext)
                .load(file)
                .into(holder.ivImage);
        holder.tvComicName.setText(comic.getTitle());
    }

    @Override
    public int getItemCount() {
        return mListComic.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_comic_item)
        AppCompatImageView ivImage;
        @BindView(R.id.tv_comic_item)
        TextView tvComicName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.setAction(Constant.PREF_OFFLINE);
            intent.putExtra(EXTRA_COMIC_DOWNLOADED, mListComic.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
