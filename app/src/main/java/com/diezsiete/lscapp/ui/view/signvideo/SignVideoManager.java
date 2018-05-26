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
    private final static String TAG = "JOSE";

    List<SignVideo> signVideos = new ArrayList<>();
    Context context;

    private ArrayList<SignVideoManager.onSingleTapUpListener> onSingleTapUpListeners;

    public boolean onlyOnePlaying = true;

    public interface onSingleTapUpListener {
        void onSingleTapUp(int position, boolean playing);
    }

    public SignVideoManager(Context context, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.context = context;
    }

    private void addSignVideo(final int position) {
        SignVideo signVideo = new SignVideo(this.context);
        signVideo.setOnSingleTapUp(new SignVideo.onSingleTapUpListener() {
            @Override
            public void onSingleTapUp(boolean playing) {
                if(onlyOnePlaying && signVideos.size() > 1 && playing){
                    for(int i = 0; i < signVideos.size(); i++){
                        if(i != position)
                            signVideos.get(i).stopAndSeekTo(0);
                    }
                }
                if(onSingleTapUpListeners != null)
                    for(int i = 0; i < onSingleTapUpListeners.size(); i++)
                        onSingleTapUpListeners.get(i).onSingleTapUp(position, playing);
            }
        });
        signVideos.add(signVideo);
    }

    public SignVideo getSignVideo() {
        if(signVideos.size() < 1)
            addSignVideo(0);
        return signVideos.get(0);
    }

    public SignVideo getSignVideo(int position) {
        if(position >= signVideos.size())
            addSignVideo(position);
        return signVideos.get(position);
    }

    public void setOnSingleTapUp(onSingleTapUpListener listener) {
        if(onSingleTapUpListeners == null)
            onSingleTapUpListeners = new ArrayList<>();
        onSingleTapUpListeners.add(listener);
    }

    public SignVideoManager unsetOnSingleTapUp() {
        if(onSingleTapUpListeners != null)
            onSingleTapUpListeners.clear();
        return this;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        if (Util.SDK_INT <= 23) {
            Log.d(TAG, "onPause : releasePlayer");
            for(SignVideo signVideo : signVideos)
                signVideo.releasePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        Log.d(TAG, "onResume : initExoPlayer");
        for(SignVideo signVideo : signVideos)
            signVideo
                    .prepare()
                    .resetPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        if(Util.SDK_INT > 23) {
            Log.d(TAG, "onStart : initExoPlayer");
            for(SignVideo signVideo : signVideos)
                signVideo.prepare().resetPlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "onStop : releasePlayer");
            for(SignVideo signVideo : signVideos)
                signVideo.releasePlayer();
        }
    }
}
