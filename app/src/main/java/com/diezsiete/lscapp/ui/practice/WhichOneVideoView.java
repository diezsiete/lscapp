package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeWhichOneVideoBinding;
import com.diezsiete.lscapp.vo.Practice;


@SuppressLint("ViewConstructor")
public class WhichOneVideoView extends PracticeView {


    public WhichOneVideoView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeWhichOneVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_which_one_video, this, false);

        GridView gridView = binding.gridView;
        /*Practice practice = practiceViewModel.getCurrentPractice();

        gridView.setSelector(R.drawable.selector_button);
        gridView.setAdapter(new WhichOneVideosAdapter(practice.getWords(), R.layout.item_practice_text));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/

        /*SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);
        videoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
        for(String video : getPractice().getVideos())
            videoPlayer.addExternalResource(video);*/

        return binding;
    }

    @Override
    protected void onDetachedFromWindow() {
        //mVideoPlayer.release();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        //mVideoPlayer.initialize();
        super.onAttachedToWindow();
    }



}
