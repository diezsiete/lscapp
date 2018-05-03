package com.diezsiete.lscapp.utils.signvideoplayer;


import android.content.Context;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class SignVideoPlayerHelper {

    private static ArrayList<SignVideoPlayer> mPlayers;


    public static void initialize() {
        if(mPlayers != null)
            for(SignVideoPlayer videoPlayer : mPlayers)
                videoPlayer.initialize();
    }

    public static void release() {
        if(mPlayers != null)
            for(SignVideoPlayer videoPlayer : mPlayers)
                videoPlayer.release();
        clear();
    }

    public static void clear() {
        if(mPlayers != null && mPlayers.size() > 0)
            mPlayers.clear();
    }

    public static void onStart() {
        if(Util.SDK_INT > 23)
            initialize();
    }
    public static void onResume() {
        if(mPlayers != null)
            for(SignVideoPlayer videoPlayer : mPlayers)
                if(Util.SDK_INT <= 23 || videoPlayer.isNull())
                    videoPlayer.initialize();
    }
    public static void onPause() {
        if(Util.SDK_INT <= 23)
            release();
    }
    public static void onStop() {
        if(Util.SDK_INT > 23)
            release();
    }

    public static SignVideoPlayer create(Context context, SimpleExoPlayerView view) {
        if(mPlayers == null)
            mPlayers = new ArrayList<>();
        SignVideoPlayer videoPlayer = new SignVideoPlayer(context, view);
        mPlayers.add(videoPlayer);
        return videoPlayer;
    }

}
