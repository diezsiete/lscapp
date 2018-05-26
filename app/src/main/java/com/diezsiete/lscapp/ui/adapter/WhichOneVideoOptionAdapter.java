/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.ItemWhichOneVideoOptionBinding;
import com.diezsiete.lscapp.ui.adapter.DataBoundListAdapter;
import com.diezsiete.lscapp.ui.adapter.DataBoundViewHolder;

import java.util.List;

public class WhichOneVideoOptionAdapter extends DataBoundListAdapter<List<String>, ItemWhichOneVideoOptionBinding> {
    private final DataBindingComponent dataBindingComponent;
    private ClickCallback clickCallback;
    private View.OnFocusChangeListener focusChangeListener;
    private View viewPrevSel;

    public WhichOneVideoOptionAdapter(DataBindingComponent dataBindingComponent, ClickCallback clickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.clickCallback = clickCallback;
    }

    @Override
    public final DataBoundViewHolder<ItemWhichOneVideoOptionBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemWhichOneVideoOptionBinding binding = createBinding(parent);
        DataBoundViewHolder<ItemWhichOneVideoOptionBinding> viewHolder = new DataBoundViewHolder<>(binding);


        binding.getRoot().setOnClickListener(view -> {
                final int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickCallback != null) {
                    clickCallback.onClick(position);
                }
            });

        return viewHolder;
    }

    @Override
    protected ItemWhichOneVideoOptionBinding createBinding(ViewGroup parent) {
        ItemWhichOneVideoOptionBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_which_one_video_option,
                        parent, false, dataBindingComponent);
        return binding;
    }

    @Override
    protected void bind(ItemWhichOneVideoOptionBinding binding, List<String> word, int position) {
        binding.setOption(TextUtils.join(" ", word));
    }

    @Override
    protected boolean areItemsTheSame(List<String> oldItem, List<String> newItem) {
        String oldItemString = TextUtils.join(" ", oldItem);
        String newItemString = TextUtils.join(" ", newItem);
        return oldItemString.equals(newItemString);
    }

    @Override
    protected boolean areContentsTheSame(List<String> oldItem, List<String> newItem) {
        return areItemsTheSame(oldItem, newItem);
    }

    public interface ClickCallback {
        void onClick(Integer optionIndex);
    }
}
