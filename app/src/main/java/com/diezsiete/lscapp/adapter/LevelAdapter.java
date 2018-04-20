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

package com.diezsiete.lscapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.databinding.DataBindingUtil;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.ItemLevelBinding;
import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.model.Level;
import com.squareup.picasso.Picasso;


import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    private Level[] mData;

    private Context mContext;

    private final Activity mActivity;
    private final Resources mResources;
    private final String mPackageName;

    public static final String DRAWABLE = "drawable";
    private static final String ICON_CATEGORY = "icon_category_";

    //private final LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(Level level);
    }

    public LevelAdapter(Activity activity) {
        mActivity = activity;
        mResources = mActivity.getResources();
        mPackageName = mActivity.getPackageName();
        //mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder((ItemLevelBinding) DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.item_level, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ItemLevelBinding binding = holder.getBinding();
        Level level = mData[position];
        binding.setLevel(level);
        binding.executePendingBindings();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Level level = mData[holder.getAdapterPosition()];
                mOnItemClickListener.onClick(level);
            }
        });

        ImageView levelImageView = holder.itemView.findViewById(R.id.level_image);
        Picasso.with(mContext).load(level.getImage())
                .resize(300, 300)
                .into(levelImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.length;
    }

    public Level getItem(int position) {
        return mData[position];
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemLevelBinding mBinding;

        public ViewHolder(ItemLevelBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ItemLevelBinding getBinding() {
            return mBinding;
        }
    }

    public void setData(Level[] data) {
        mData = data;
        notifyDataSetChanged();
    }
}
