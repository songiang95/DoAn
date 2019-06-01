package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.songiang.readebookandmanga.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.DetailViewHolder> {

    private List<String> data;
    private OnItemClickListener mListener;
    public ChapterAdapter(List data, OnItemClickListener listener)
    {
        this.data = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_chap_item, viewGroup, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder detailViewHolder, int i) {
        final String url = data.get(i);
        if(url != null)
        {
            detailViewHolder.tvChapNumb.setText(Integer.toString(i+1));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_chap_number)
        TextView tvChapNumb;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null)
                mListener.onItemClick(v, data.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, String url, int position);
    }
}
