package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.databinding.PracticeTranslateVideoBinding;


@SuppressLint("ViewConstructor")
public class TranslateVideoView extends PracticeView {

    public TranslateVideoView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeTranslateVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_translate_video, this, false);


        /*
        SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);
        mVideoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
        for(String video : getPractice().getVideos())
            mVideoPlayer.addExternalResource(video);
            */

        //ViewGroup wordSelectorView = container.findViewById(R.id.word_selector);
        //WordSelector wordSelector = new WordSelector(wordSelectorView);
        //wordSelector.setOptions(getPractice().getOptions());


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
