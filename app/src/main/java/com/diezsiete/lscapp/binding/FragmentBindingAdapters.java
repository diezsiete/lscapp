/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diezsiete.lscapp.binding;

import android.animation.ObjectAnimator;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

//import com.bumptech.glide.Glide;

//import com.bumptech.glide.Glide;
import com.diezsiete.lscapp.ui.dictionary.DictionaryFragment;
import com.diezsiete.lscapp.ui.lesson.LessonFragment;
import com.diezsiete.lscapp.util.signvideo.SignVideoManager;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    private final static String TAG = "FragmentBindingAdapter";

    final Fragment fragment;


    private ObjectAnimator progressAnimator;

    private AccelerateInterpolator accelerateInterpolator;

    private AccelerateInterpolator getInterpolator(){
        if(accelerateInterpolator == null)
            accelerateInterpolator = new AccelerateInterpolator();
        return accelerateInterpolator;
    }

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        //Glide.with(fragment).load(url).into(imageView);
        Picasso.with(fragment.getContext()).load(url).into(imageView);
    }

    @BindingAdapter("videoUrl")
    public void bindVideo(SimpleExoPlayerView playerView, String url) {
        Log.d("JOSE", "bindVideo : " + url);
        if(url != null) {
            ((DictionaryFragment) fragment).videoManager.getSignVideo()
                    .setPlayer(playerView)
                    .clearResources()
                    .addExternalResource(url)
                    .prepare()
                    .play();
        }else{
            ((DictionaryFragment) fragment).videoManager.getSignVideo().stop();
        }
    }


    @BindingAdapter(value={"practiceVideo", "position"}, requireAll = false)
    public void bindPracticeVideo(SimpleExoPlayerView playerView, List<String> urls, int position) {
        Log.d("JOSE", TAG + " bindPracticeVideo position[" + position + "]" );
        if(urls != null && urls.size() > 0) {
            SignVideoManager videoManager = ((LessonFragment) fragment).videoManager;
            videoManager.getSignVideo(position)
                    .setPlayer(playerView)
                    .clearResources()
                    .addExternalResources(urls)
                    .prepare()
                    .play();
        }
    }

    @BindingAdapter("progressAnimation")
    public void setProgress(ProgressBar progressBar, int value) {
        if(progressAnimator == null) {
            progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 0);
            progressAnimator.setDuration(600);
            progressAnimator.setInterpolator(getInterpolator());
        }
        progressAnimator.setIntValues(progressBar.getProgress(), value);
        progressAnimator.start();
    }

    protected void practiceEndVisibilityAnimation(View view, boolean show, int startDelay){
        if(show)
            view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(show ? 1 : 0)
                .scaleY(show ? 1 : 0)
                .alpha(show ? 1 : 0)
                .setInterpolator(getInterpolator())
                .setStartDelay(startDelay)
                .start();
    }

    @BindingAdapter(value={"practiceEndVisible", "startDelay"}, requireAll = false)
    public void practiceEndVisibile(View view, int progress, int startDelay) {
        if(progress == 100)
            practiceEndVisibilityAnimation(view, true, startDelay);
    }

    @BindingAdapter(value={"practiceEndInvisible", "startDelay"}, requireAll = false)
    public void practiceEndInvisibile(View view, int value, int startDelay) {
        if(value == 100)
            practiceEndVisibilityAnimation(view, false, startDelay);
    }

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("buttonEnable")
    public static void buttonDisable(ImageButton button, boolean enable) {
        button.setEnabled(enable);
        button.setClickable(enable);
        button.setAlpha(enable ? 1f : 0.5f);
    }
}
