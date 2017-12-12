package com.example.wonyoungkim.embeddedproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-12-06.
 */

public class ThirdFragment extends Fragment {
    @BindView(R.id.imageView)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(this, view);

        Glide.with(this)
                .load(R.drawable.third)
                .centerCrop()
                .into(imageView);
        return view;
    }
}
