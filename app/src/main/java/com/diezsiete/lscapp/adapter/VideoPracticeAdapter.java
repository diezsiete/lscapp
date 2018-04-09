/*
 * Copyright 2015 Google Inc.
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

package com.lscapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.lscapp.R;
import com.lscapp.helper.SignVideoPlayerHelper;
import com.lscapp.widget.SignVideoPlayer;

import java.util.ArrayList;

/**
 * A simple adapter to display a options of a quiz.
 */
public class VideoQuizAdapter extends BaseAdapter {

    private final String[] mOptions;
    private final int mLayoutId;
    private final String[] mAlphabet;

    private int videoPlayers = 0;

    /**
     * Creates an {@link VideoQuizAdapter}.
     *
     * @param options The options to add to the adapter.
     * @param layoutId Must consist of a single {@link TextView}.
     */
    public VideoQuizAdapter(String[] options, @LayoutRes int layoutId) {
        mOptions = options;
        mLayoutId = layoutId;
        mAlphabet = null;
    }

    /**
     * Creates an {@link VideoQuizAdapter}.
     *
     * @param options The options to add to the adapter.
     * @param layoutId Must consist of a single {@link TextView}.
     * @param context The context for the adapter.
     * @param withPrefix True if a prefix should be given to all items.
     */
    public VideoQuizAdapter(String[] options, @LayoutRes int layoutId,
                            Context context, boolean withPrefix) {
        mOptions = options;
        mLayoutId = layoutId;
        if (withPrefix) {
            mAlphabet = context.getResources().getStringArray(R.array.alphabet);
        } else {
            mAlphabet = null;
        }
    }

    @Override
    public int getCount() {
        return mOptions.length;
    }

    @Override
    public String getItem(int position) {
        return mOptions[position];
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
            SignVideoPlayer videoPlayer = createSignVideoPlayer(position, convertView, (AbsListView) parent);
            if(position + 1 == getCount()) {
                SignVideoPlayerHelper.initialize();
            }
        }
        return convertView;
    }

    private SignVideoPlayer createSignVideoPlayer(final int position, View view, final AbsListView listView) {
        Context context = view.getContext();
        SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.video_answer_view);
        SignVideoPlayer videoPlayer = SignVideoPlayerHelper.create(context, exoPlayerView);


        String videoName = getItem(position);
        Resources res = context.getResources();
        int videoId = res.getIdentifier(videoName, "raw", context.getPackageName());
        videoPlayer.addLocalResource(videoId);


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
