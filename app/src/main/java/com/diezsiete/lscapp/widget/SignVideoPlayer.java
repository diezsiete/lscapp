/*
 * V 0.1
 */
package com.diezsiete.lscapp.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SignVideoPlayer {

    private static final String TAG = SignVideoPlayer.class.getSimpleName();

    private static final int URI_EXTERNAL = 1;
    private static final int URI_LOCAL_RAW = 2;
    private static final int URI_LOCAL_ASSET = 3;

    private GestureDetector gestureDetector;
    private Context context;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private LinkedHashMap<Uri, Integer> resources;
    private ArrayList<onSingleTapUpListener> onSingleTapUpListeners;

    public boolean playWhenReady = true;
    public boolean onSingleTapUpPlayStop = false;
    public boolean loop = true;

    public interface onSingleTapUpListener {
        void onSingleTapUp(boolean playing);
    }

    public SignVideoPlayer(Context context, SimpleExoPlayerView playerView) {
        this.context = context;
        this.playerView = playerView;
        resources = new LinkedHashMap<>();

        TouchListener touchListener = new TouchListener();
        gestureDetector = new GestureDetector(context, touchListener);

        this.playerView.setOnTouchListener(new TouchListener());
    }

    private ExtractorMediaSource buildExtractorExternal(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri);
    }

    private ExtractorMediaSource buildExtractorLocal(Uri uri) {
        DataSpec dataSpec = new DataSpec(uri);
        final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);
        try {
            rawResourceDataSource.open(dataSpec);
        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return rawResourceDataSource;
            }
        };

        DefaultDataSourceFactory defaultDataSource = new DefaultDataSourceFactory(context, null, factory);
        return new ExtractorMediaSource.Factory(defaultDataSource).createMediaSource(uri);
    }

    private ExtractorMediaSource buildExtractorAsset(Uri uri) {
        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return new AssetDataSource(context);
            }
        };
        DefaultDataSourceFactory defaultDataSource = new DefaultDataSourceFactory(context, null, factory);
        return new ExtractorMediaSource.Factory(defaultDataSource).createMediaSource(uri);
    }

    private MediaSource buildMediaSource() {
        ArrayList<ExtractorMediaSource> extractors = new ArrayList<>();

        for(LinkedHashMap.Entry<Uri, Integer> pair : resources.entrySet()){
            ExtractorMediaSource extractor = null;
            switch(pair.getValue()){
                case URI_EXTERNAL:
                    extractor = buildExtractorExternal(pair.getKey());
                    break;
                case URI_LOCAL_RAW:
                    extractor = buildExtractorLocal(pair.getKey());
                    break;
                case URI_LOCAL_ASSET:
                    extractor = buildExtractorAsset(pair.getKey());
            }
            extractors.add(extractor);
        }

        MediaSource mediaSource = null;
        if(extractors.size() > 1)
            mediaSource = new ConcatenatingMediaSource(extractors.toArray(new ExtractorMediaSource[extractors.size()]));
        else
            mediaSource = extractors.get(0);

        return mediaSource;
    }

    public SignVideoPlayer addExternalResource(String uri) {
        resources.put(Uri.parse(uri), URI_EXTERNAL);
        return this;
    }

    public SignVideoPlayer addLocalResource(int rawResourceId) {
        resources.put(RawResourceDataSource.buildRawResourceUri(rawResourceId), URI_LOCAL_RAW);
        return this;
    }

    public SignVideoPlayer addLolcalResourceAsset(String assetName) {
        resources.put(Uri.parse("assets:///"+assetName), URI_LOCAL_ASSET);
        return this;
    }

    public void initialize()
    {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());

        player.setVolume(0f);
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        if(this.loop)
            player.setRepeatMode(Player.REPEAT_MODE_ALL);


        MediaSource mediaSource = buildMediaSource();

        player.prepare(mediaSource, true, false);

    }

    public void release() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public boolean isNull() {
        return player == null;
    }


    @SuppressLint("InlinedApi")
    public void hideSystemUi() {
        if(playerView != null)
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
    }

    /**
     * https://stackoverflow.com/questions/41414974/exoplayer-hide-playbackcontrolview-onclick-not-ontouch
     */
    private class TouchListener extends GestureDetector.SimpleOnGestureListener
            implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            v.performClick();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            playWhenReady = !playWhenReady;
            if(onSingleTapUpPlayStop)
                player.setPlayWhenReady(playWhenReady);

            if(onSingleTapUpListeners != null)
                for(int i = 0; i < onSingleTapUpListeners.size(); i++)
                    onSingleTapUpListeners.get(i).onSingleTapUp(playWhenReady);

            return true;
        }
    }

    public void setOnSingleTapUp(onSingleTapUpListener listener) {
        if(onSingleTapUpListeners == null)
            onSingleTapUpListeners = new ArrayList<>();
        onSingleTapUpListeners.add(listener);
    }
}
