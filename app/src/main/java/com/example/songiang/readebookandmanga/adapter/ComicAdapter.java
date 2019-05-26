package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.model.Comic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.MyViewHolder> {

    private List<Comic> data;
    private Context mContext;
    private OnItemClickListener listener;

    public ComicAdapter(Context context, List data, OnItemClickListener listener) {
        this.data = data;
        mContext = context;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.layout_comic_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        final Comic temp = data.get(i);
        if (temp != null) {
            Glide.with(mContext).load(temp.getImage()).into(viewHolder.ivImage);
            viewHolder.tvComicName.setText(temp.getName());
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_comic_item)
        ImageView ivImage;
        @BindView(R.id.tv_comic_item)
        TextView tvComicName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, data.get(getAdapterPosition()));
            }
        }

    }

    public interface OnItemClickListener{
        void onItemClick(View v, Comic comic);
    }

}
