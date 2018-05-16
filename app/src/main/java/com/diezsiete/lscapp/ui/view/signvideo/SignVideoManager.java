package com.diezsiete.lscapp.ui.view.signvideo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;


import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SignVideoManager implements LifecycleObserver {
    private final static String TAG = "SignVideoManager";

    List<SignVideoPlayer> signVideos = new ArrayList<>();
    Context context;

    public SignVideoManager(Context context, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.context = context;
    }


    public SignVideoPlayer getSignVideo() {
        if(signVideos.size() < 1)
            signVideos.add(new SignVideoPlayer(this.context));
        return signVideos.get(0);
    }

    public SignVideoPlayer getSignVideo(int position) {
        if(position >= signVideos.size())
            signVideos.add(new SignVideoPlayer(this.context));
        return signVideos.get(position);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        if (Util.SDK_INT <= 23) {
            Log.d(TAG, "onPause : releasePlayer");
            for(SignVideoPlayer signVideo : signVideos)
                signVideo.releasePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        Log.d(TAG, "onResume : initExoPlayer");
        for(SignVideoPlayer signVideo : signVideos)
            signVideo.initExoPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        if(Util.SDK_INT > 23) {
            Log.d(TAG, "onStart : initExoPlayer");
            for(SignVideoPlayer signVideo : signVideos)
                signVideo.initExoPlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "onStop : releasePlayer");
            for(SignVideoPlayer signVideo : signVideos)
                signVideo.releasePlayer();
        }
    }

}
