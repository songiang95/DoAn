package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.database.Repository;
import com.example.songiang.readebookandmanga.ebook.reading.ReadEbookActivity;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.MyEbookViewHolder> {


    public static final String EXTRA_PDF = "pdf";
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
            Intent intent = new Intent(mContext, ReadEbookActivity.class);
            intent.putExtra(EXTRA_PDF, data.get(getAdapterPosition()).getPdfLink());
            mContext.startActivity(intent);
        }

        @OnClick(R.id.iv_favorite)
        public void onClickFavorite() {
            final Ebook ebook = data.get(getAdapterPosition());
            if (!Utils.isFavorited(ebook)) {
                new Thread() {
                    @Override
                    public void run() {
                        Repository repository = new Repository(mContext);
                        repository.getFavoriteEbookDao().insertEbookFavorite(ebook);
                    }
                }.start();
                Hawk.put(ebook.getTitle(), true);
                Glide.with(itemView).load(R.drawable.ic_favorite_red_24dp).into(ivFavorite);
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        Repository repository = new Repository(mContext);
                        repository.getFavoriteEbookDao().deleteFavoriteEbook(ebook);
                    }
                }.start();
                Hawk.put(ebook.getTitle(), false);
                Glide.with(itemView).load(R.drawable.ic_favorite_gray_24dp).into(ivFavorite);
            }
        }
    }

}
