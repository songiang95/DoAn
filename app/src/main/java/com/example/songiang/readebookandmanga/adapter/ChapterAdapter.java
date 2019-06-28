package com.example.songiang.readebookandmanga.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.model.Chapter;
import com.example.songiang.readebookandmanga.model.Comic;
import com.example.songiang.readebookandmanga.model.ComicDownloaded;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.DetailViewHolder> {

    private List<String> mChaptersOnline;
    private OnItemClickListener mCallback;
    private boolean isSelectionMode;
    private boolean isReadOnline;
    private Map<Integer, String> dataSelected;
    private Comic mComic;
    private ComicDownloaded mComicDownloaded;
    private ArrayList<Chapter> mChaptersOffline;

    public ChapterAdapter(List data, Comic comic) {
        this.mChaptersOnline = data;
        dataSelected = new LinkedHashMap();
        this.isReadOnline = true;
        this.mComic = comic;
    }

    public ChapterAdapter(List<Chapter> chapters, ComicDownloaded comicDownloaded) {
        this.isReadOnline = false;
        mChaptersOffline = (ArrayList) chapters;
        mComicDownloaded = comicDownloaded;
    }

    public void setItemSelection(boolean bool) {
        isSelectionMode = bool;
    }

    public Map getSelectedItem() {
        return dataSelected;
    }

    public void clearSelectedItem() {
        dataSelected.clear();
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
        if (isReadOnline) {
            final String url = mChaptersOnline.get(i);
            if (url != null) {
                detailViewHolder.tvChapNumb.setText(Integer.toString(i + 1));
                if (Hawk.get(Constant.PREF_CONTINUE_CHAP_NUMB + mComic.getName(), 0) - 1 == i && !isSelectionMode) {
                    detailViewHolder.frChapter.setBackgroundResource(R.drawable.ripple_chap_item_clicked);
                } else {
                    detailViewHolder.frChapter.setBackgroundResource(R.drawable.ripple_chap_item);
                }
            }
        } else {
            final Chapter chapter = mChaptersOffline.get(i);
            String chapterNumb = Integer.toString(chapter.getChapterNumb());
            detailViewHolder.tvChapNumb.setText(chapterNumb);
            if (Hawk.get(Constant.PREF_CONTINUE_CHAP_NUMB + mComicDownloaded.getTitle(), 0) - 1 == i && !isSelectionMode) {
                detailViewHolder.frChapter.setBackgroundResource(R.drawable.ripple_chap_item_clicked);
            } else {
                detailViewHolder.frChapter.setBackgroundResource(R.drawable.ripple_chap_item);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isReadOnline) {
            return mChaptersOnline.size();
        } else {
            return mChaptersOffline.size();
        }
    }

    class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_chap_number)
        TextView tvChapNumb;
        @BindView(R.id.fr_chapter)
        FrameLayout frChapter;
        private boolean isSelected;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!isSelectionMode) {
                if (isReadOnline) {
                    if (mCallback != null)
                        mCallback.onItemClick(v, mChaptersOnline.get(getAdapterPosition()), getAdapterPosition());
                } else {
                    if (mCallback != null)
                        mCallback.onItemClick(v, mComicDownloaded.getTitle(), mChaptersOffline.get(getAdapterPosition()).getChapterNumb());
                }
            } else {
                if (!isSelected) {
                    isSelected = true;
                    dataSelected.put(getAdapterPosition() + 1, mChaptersOnline.get(getAdapterPosition()));
                    v.setBackgroundResource(R.drawable.ripple_chap_item_clicked);
                } else {
                    isSelected = false;
                    dataSelected.remove(getAdapterPosition() + 1);
                    v.setBackgroundResource(R.drawable.ripple_chap_item);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String url, int position);
    }

    public void setCallback(OnItemClickListener callback) {
        this.mCallback = callback;
    }
}
