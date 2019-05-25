package com.example.songiang.readebookandmanga.View;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.songiang.readebookandmanga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaListFragment extends Fragment {


    public MangaListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout
                .fragment_manga_list, container, false);
    }

}
