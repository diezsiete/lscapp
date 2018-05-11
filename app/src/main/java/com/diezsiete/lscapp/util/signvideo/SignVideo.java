package com.diezsiete.lscapp.util.signvideo;

import android.content.Context;
import android.net.Uri;

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
    private static final int URI_EXTERNAL = 1;

    private LinkedHashMap<Uri, Integer> resources;

    private SimpleExoPlayer player;
    private Context context;

    SignVideo(Context context) {
        this.context = context;
        resources = new LinkedHashMap<>();
    }

    public SignVideo setPlayer(SimpleExoPlayerView view){
        initExoPlayer();
        view.setPlayer(player);
        return this;
    }

    public SignVideo addExternalResources(List<String> urls) {
        for(String url : urls)
            resources.put(Uri.parse(url), URI_EXTERNAL);
        return this;
    }

    public SignVideo addExternalResource(String url) {
        resources.put(Uri.parse(url), URI_EXTERNAL);
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
        if(player != null)
            player.setPlayWhenReady(false);
    }

    public void play() {
        if(player != null)
            player.setPlayWhenReady(true);
    }

    public void initExoPlayer() {
        if(player  == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            //player.setVolume(0f);
            player.setPlayWhenReady(true);
            player.seekTo(0, 0);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
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
}
