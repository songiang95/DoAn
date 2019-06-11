package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.songiang.readebookandmanga.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.DetailViewHolder> {

    private List<String> data;
    private OnItemClickListener mListener;
    private boolean isSelectionMode;
    private Map<Integer, String> dataSelected;

    public ChapterAdapter(List data, OnItemClickListener listener) {
        this.data = data;
        mListener = listener;
        dataSelected = new LinkedHashMap();
    }

    public void setItemSelection(boolean bool) {
        isSelectionMode = bool;
    }

    public Map getSelectedItem() {
        return dataSelected;
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
        if (url != null) {
            detailViewHolder.tvChapNumb.setText(Integer.toString(i + 1));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_chap_number)
        TextView tvChapNumb;
        private boolean isSelected;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!isSelectionMode) {
                if (mListener != null)
                    mListener.onItemClick(v, data.get(getAdapterPosition()), getAdapterPosition());
            } else {
                // TODO: 6/11/2019 select multiple item
                if (!isSelected) {
                    isSelected = true;
                    dataSelected.put(getAdapterPosition() + 1, data.get(getAdapterPosition()));
                    v.setBackgroundResource(R.drawable.ripple_chap_item_clicked);
                } else {
                    isSelected = false;
                    dataSelected.remove(getAdapterPosition()+1);
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String url, int position);
    }
}
