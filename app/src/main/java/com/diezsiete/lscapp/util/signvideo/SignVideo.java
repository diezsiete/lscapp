package com.diezsiete.lscapp.util.signvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SignVideo {
    private static final String TAG = SignVideo.class.getSimpleName();

    private static final int URI_EXTERNAL = 1;

    private LinkedHashMap<Uri, Integer> resources;

    private SimpleExoPlayer player;
    private Context context;

    private GestureDetector gestureDetector;
    private ImageButton playButton;
    private ProgressBar progressBar;
    private boolean stateBuffering = false;
    private final int animTime;
    private ComponentListener componentListener;
    private SimpleExoPlayerView playerView;
    private ArrayList<onSingleTapUpListener> onSingleTapUpListeners;

    public boolean playWhenReadySignPlayer = true;
    public boolean onSingleTapUpPlayStop = true;

    public interface onSingleTapUpListener {
        void onSingleTapUp(boolean playing);
    }

    SignVideo(Context context) {
        this.context = context;
        resources = new LinkedHashMap<>();
        animTime = this.context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public SignVideo setPlayer(SimpleExoPlayerView view){
        initExoPlayer();
        view.setPlayer(player);
        playButton = view.findViewById(R.id.exo_play);
        progressBar = view.findViewById(R.id.signplayer_pb);
        playerView = view;

        TouchListener touchListener = new TouchListener();
        gestureDetector = new GestureDetector(context, touchListener);

        this.playerView.setOnTouchListener(new TouchListener());
        playerView.setShowMultiWindowTimeBar(true);
        if(progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        forcePlaybackControlsVisible();
        return this;
    }

    private void addExternalUrl(String url) {
        int pos = url.lastIndexOf('/') + 1;
        Uri parsedUrl = Uri.parse(url.substring(0, pos) + Uri.encode(url.substring(pos)));
        Log.d("JOSE", "parsed1 : " + parsedUrl.toString());
        Uri parsedUrl2 = Uri.parse(url);
        Log.d("JOSE", "parsed2 : " + parsedUrl2);
        resources.put(parsedUrl, URI_EXTERNAL);
    }

    public SignVideo addExternalResources(List<String> urls) {
        for(String url : urls)
            addExternalUrl(url);
        return this;
    }

    public SignVideo addExternalResource(String url) {
        addExternalUrl(url);
        return this;
    }

    public SignVideo clearResources() {
        resources.clear();
        return this;
    }

    public SignVideo prepare() {
        MediaSource mediaSource = buildMediaSource();
        initExoPlayer();
        player.prepare(mediaSource, true, false);
        return this;
    }

    public void stop() {
        if(player != null) {
            playWhenReadySignPlayer = false;
            player.setPlayWhenReady(false);
        }
    }

    public void play() {
        if(player != null) {
            playWhenReadySignPlayer = true;
            player.setPlayWhenReady(true);
        }
    }

    public void initExoPlayer() {
        if(player  == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            //player.setVolume(0f);
            player.setPlayWhenReady(playWhenReadySignPlayer);
            player.seekTo(0, 0);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);

            componentListener = new ComponentListener();
            player.addListener(componentListener);
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }

    public void forcePlaybackControlsVisible() {
        this.playerView.setControllerShowTimeoutMs(0);
        this.playerView.setControllerHideOnTouch(false);
    }

    public void setOnSingleTapUp(onSingleTapUpListener listener) {
        if(onSingleTapUpListeners == null)
            onSingleTapUpListeners = new ArrayList<>();
        onSingleTapUpListeners.add(listener);
    }

    private MediaSource buildMediaSource() {
        ArrayList<ExtractorMediaSource> extractors = new ArrayList<>();

        for(LinkedHashMap.Entry<Uri, Integer> pair : resources.entrySet()){
            ExtractorMediaSource extractor = null;
            switch(pair.getValue()){
                case URI_EXTERNAL:
                    extractor = buildExtractorExternal(pair.getKey());
                    break;
            }
            if(extractor != null)
                extractors.add(extractor);
        }

        MediaSource mediaSource = null;
        if(extractors.size() > 1) {
            mediaSource = new ConcatenatingMediaSource(extractors.toArray(new ExtractorMediaSource[extractors.size()]));
        }else
            mediaSource = extractors.get(0);

        return mediaSource;
    }

    private ExtractorMediaSource buildExtractorExternal(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("lscapp"))
                .createMediaSource(uri);
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


    /**
     * https://stackoverflow.com/questions/41414974/exoplayer-hide-playbackcontrolview-onclick-not-ontouch
     */
    private class TouchListener extends GestureDetector.SimpleOnGestureListener
            implements View.OnTouchListener{

        TouchListener(){
            super();
        }
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


}
