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
package com.diezsiete.lscapp.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Level;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    private Level[] mData;


    public static final String DRAWABLE = "drawable";
    private static final String ICON_CATEGORY = "icon_category_";


    private Callback mCallback;

    public interface Callback {
        void onLevelClick(Level level);
    }

    public LevelAdapter(Level[] levels) {
        mData = levels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.length;
    }

    public Level getItem(int position) {
        return mData[position];
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.level_title) TextView titleTextView;
        @BindView(R.id.level_image) ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(int position) {
            final Level level = mData[position];

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onLevelClick(level);
                }
            });
            titleTextView.setText(level.getTitle());
            Picasso.with(itemView.getContext()).load(level.getImage())
                    .resize(200, 200)
                    .into(imageView);
        }


    }

    public void setData(Level[] data) {
        mData = data;
        notifyDataSetChanged();
    }
}
