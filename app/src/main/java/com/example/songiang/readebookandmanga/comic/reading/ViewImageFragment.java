package com.example.songiang.readebookandmanga.comic.reading;


import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.adapter.MyPagerAdapter;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.chrisbanes.photoview.PhotoView;

import org.w3c.dom.Text;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewImageFragment extends Fragment {

    @BindView(R.id.photo_view)
    PhotoView photoView;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_error_loading)
    TextView errorLoading;

    public ViewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_image, container, false);
        ButterKnife.bind(this, view);
        String imgName = getArguments().getString(MyPagerAdapter.EXTRA_IMAGE_NAME);
        boolean state = getArguments().getBoolean(MyPagerAdapter.EXTRA_STATE);
        if (state) {
            pbLoading.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(imgName)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pbLoading.setVisibility(View.GONE);
                            errorLoading.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pbLoading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(photoView);
        } else {
            pbLoading.setVisibility(View.GONE);
            File file = new File(imgName);
            Glide.with(getActivity())
                    .load(file)
                    .into(photoView);
        }
        return view;
    }


}
