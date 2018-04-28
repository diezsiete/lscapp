/*
 * V 0.1
 */
package com.diezsiete.lscapp.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.diezsiete.lscapp.R;
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
    private ComponentListener componentListener;
    private ImageButton playButton;
    private ProgressBar progressBar;

    private boolean stateBuffering = false;
    private final int animTime;


    public boolean playWhenReadySignPlayer = true;
    public boolean onSingleTapUpPlayStop = false;
    public boolean loop = true;

    public interface onSingleTapUpListener {
        void onSingleTapUp(boolean playing);
    }

    public SignVideoPlayer(Context context, SimpleExoPlayerView playerView) {
        this.context = context;
        this.playerView = playerView;
        resources = new LinkedHashMap<>();


        playButton = playerView.findViewById(R.id.exo_play);
        progressBar = playerView.findViewById(R.id.signplayer_pb);

        TouchListener touchListener = new TouchListener();
        gestureDetector = new GestureDetector(context, touchListener);

        this.playerView.setOnTouchListener(new TouchListener());
        playerView.setShowMultiWindowTimeBar(true);

        animTime = this.context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        forcePlaybackControlsVisible();
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

    public void clearResources() {
        resources.clear();
    }

    public void initialize() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());

        player.setVolume(0f);
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReadySignPlayer);
        player.seekTo(currentWindow, playbackPosition);
        if(this.loop)
            player.setRepeatMode(Player.REPEAT_MODE_ALL);

        progressBar.setVisibility(View.VISIBLE);

        MediaSource mediaSource = buildMediaSource();

        componentListener = new ComponentListener();
        player.addListener(componentListener);

        player.prepare(mediaSource, true, false);
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

    public void release() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReadySignPlayer = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }

    public boolean isNull() {
        return player == null;
    }

    public void forcePlaybackControlsVisible() {
        this.playerView.setControllerShowTimeoutMs(0);
        this.playerView.setControllerHideOnTouch(false);
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
        if(extractors.size() > 1) {
            mediaSource = new ConcatenatingMediaSource(extractors.toArray(new ExtractorMediaSource[extractors.size()]));
        }else
            mediaSource = extractors.get(0);

        return mediaSource;
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
            playWhenReadySignPlayer = !playWhenReadySignPlayer;
            if(onSingleTapUpPlayStop)
                player.setPlayWhenReady(playWhenReadySignPlayer);

            if(onSingleTapUpListeners != null)
                for(int i = 0; i < onSingleTapUpListeners.size(); i++)
                    onSingleTapUpListeners.get(i).onSingleTapUp(playWhenReadySignPlayer);

            return true;
        }
    }

    public void setOnSingleTapUp(onSingleTapUpListener listener) {
        if(onSingleTapUpListeners == null)
            onSingleTapUpListeners = new ArrayList<>();
        onSingleTapUpListeners.add(listener);
    }

    private class ComponentListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            playWhenReadySignPlayer = playWhenReady;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    stateBuffering = true;
                    break;
                case Player.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    if(stateBuffering && playWhenReady) {
                        stateBuffering = false;
                        progressBar.setVisibility(View.GONE);
                    }else
                        buttonAnimation(playWhenReady);
                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
        }
    }


    private void buttonAnimation(final boolean playWhenReady) {
        final View button = playButton;

        button.setVisibility(View.VISIBLE);
        button.animate().setDuration(animTime).alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                button.setVisibility(View.VISIBLE);

                if(playWhenReady) {
                    button.animate().setDuration(animTime).alpha(0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            button.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
