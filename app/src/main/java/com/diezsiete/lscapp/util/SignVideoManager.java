package com.diezsiete.lscapp.util;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class SignVideoManager implements LifecycleObserver {
    private final static String TAG = "SignVideoManager";
    private SimpleExoPlayer player;
    private Context context;

    public SignVideoManager(Context context, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.context = context;
    }

    public SignVideoManager setPlayer(SimpleExoPlayerView view){
        initExoPlayer();
        view.setPlayer(player);
        return this;
    }

    public SignVideoManager addExternalResource(String url) {
        MediaSource mediaSource = buildMediaSourceSingle(url);
        //Looping
        MediaSource mediaSourceLoop = new LoopingMediaSource(mediaSource);

        initExoPlayer();
        player.prepare(mediaSourceLoop, true, false);

        return this;
    }

    public SignVideoManager stop() {
        if(player != null)
            player.setPlayWhenReady(false);
        return this;
    }

    public SignVideoManager play() {
        if(player != null)
            player.setPlayWhenReady(true);
        return this;
    }

    private void initExoPlayer() {
        if(player  == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            //player.setVolume(0f);
            player.setPlayWhenReady(true);
            player.seekTo(0, 0);
        }
    }

    private MediaSource buildMediaSourceSingle(String url) {
        Uri uri = Uri.parse(url);
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        if (Util.SDK_INT <= 23) {
            Log.d(TAG, "onPause : releasePlayer");
            releasePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        Log.d(TAG, "onResume : initExoPlayer");
        initExoPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        if(Util.SDK_INT > 23) {
            Log.d(TAG, "onStart : initExoPlayer");
            initExoPlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "onStop : releasePlayer");
            releasePlayer();
        }
    }

}
