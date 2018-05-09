package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.vo.ShowSign;


@SuppressLint("ViewConstructor")
public class ShowSignView extends PracticeView {


    public ShowSignView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeShowSignBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_show_sign, this, false);


        binding.setPractice(practiceViewModel.getCurrentPracticeWithData());

        /*SimpleExoPlayerView exoPlayerView = container.findViewById(R.id.video_view);
        mVideoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
        for(String video : getPractice().getVideos()) {
            mVideoPlayer.addExternalResource(video);
        }*/

        //mTextWord = (TextView) container.findViewById(R.id.tv_show_sign);
        //mTextWord.setText("diarrea");


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
