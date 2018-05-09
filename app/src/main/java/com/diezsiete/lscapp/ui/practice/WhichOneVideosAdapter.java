package com.diezsiete.lscapp.ui.practice;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.utils.signvideoplayer.SignVideoPlayerHelper;
import com.diezsiete.lscapp.utils.signvideoplayer.SignVideoPlayer;

import java.util.List;

/**
 * A simple adapter to display a options of a quiz.
 */
public class WhichOneVideosAdapter extends BaseAdapter {

    private final List<String> mOptions;
    private final int mLayoutId;


    private int videoPlayers = 0;

    /**
     * @param options The options to add to the adapter.
     * @param layoutId Must consist of a single {@link TextView}.
     */
    public WhichOneVideosAdapter(List<String> options, @LayoutRes int layoutId) {
        mOptions = options;
        mLayoutId = layoutId;
    }



    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public String getItem(int position) {
        return mOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        /* Important to return tru ein order to get checked items from this adapter correctly */
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(mLayoutId, parent, false);
        }
        if(videoPlayers <= position){
            videoPlayers++;
            /*SignVideoPlayer videoPlayer = createSignVideoPlayer(position, convertView, (AbsListView) parent);
            if(position + 1 == getCount()) {
                SignVideoPlayerHelper.initialize();
            }*/
        }
        return convertView;
    }

    private SignVideoPlayer createSignVideoPlayer(final int position, View view, final AbsListView listView) {
        Context context = view.getContext();
        SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.video_answer_view);
        SignVideoPlayer videoPlayer = SignVideoPlayerHelper.create(context, exoPlayerView);


        String videoSrc = getItem(position);
        videoPlayer.addExternalResource(videoSrc);
        //Resources res = context.getResources();
        //int videoId = res.getIdentifier(videoName, "raw", context.getPackageName());
        //videoPlayer.addLocalResource(videoId);


        videoPlayer.setOnSingleTapUp(new SignVideoPlayer.onSingleTapUpListener() {
            @Override
            //TODO se pierde la animación de seleccion
            public void onSingleTapUp(boolean playing) {
                listView.requestFocusFromTouch();
                listView.performItemClick(listView.getChildAt(position), position,
                        listView.getAdapter().getItemId(position));
                listView.setSelection(position);
            }

        });

        return videoPlayer;
    }
}