package com.example.songiang.readebookandmanga.comic.reading;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.songiang.readebookandmanga.R;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewImageFragment extends Fragment {

    @BindView(R.id.photo_view)
    PhotoView photoView;
    public ViewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_image, container, false);
        ButterKnife.bind(this,view);
        String imgUrl = getArguments().getString("URL");
        Glide.with(getActivity()).load(imgUrl).into(photoView);
        return view;
    }

}
