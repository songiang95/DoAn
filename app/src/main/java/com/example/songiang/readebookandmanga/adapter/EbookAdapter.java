package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
            Glide.with(mContext).load(ebook.getmCover()).into(holder.ivBookCover);
            holder.tvBookTitle.setText(ebook.getmTitle());
            holder.tvAuthor.setText(ebook.getmAuthorName());
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
        @BindView(R.id.tv_book_type)
        TextView tvBookType;

        public MyEbookViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btn_read)
        public void onClickRead() {

        }

        @OnClick(R.id.iv_favorite)
        public void onClickFavorite() {

        }
    }

}
