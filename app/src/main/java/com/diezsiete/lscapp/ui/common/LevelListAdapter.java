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

package com.diezsiete.lscapp.ui.common;



import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.ItemLevelBinding;
import com.diezsiete.lscapp.vo.Level;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link Level} class.
 */
public class LevelListAdapter extends DataBoundListAdapter<Level, ItemLevelBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final LevelClickCallback levelClickCallback;

    public LevelListAdapter(DataBindingComponent dataBindingComponent, LevelClickCallback levelClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.levelClickCallback = levelClickCallback;
    }

    @Override
    protected ItemLevelBinding createBinding(ViewGroup parent) {
        ItemLevelBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_level,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Level level = binding.getLevel();
            if (level != null && levelClickCallback != null) {
                levelClickCallback.onClick(level);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemLevelBinding binding, Level item) {
        binding.setLevel(item);
    }

    @Override
    protected boolean areItemsTheSame(Level oldItem, Level newItem) {
        return Objects.equals(oldItem.levelId, newItem.levelId);
    }

    @Override
    protected boolean areContentsTheSame(Level oldItem, Level newItem) {
        return Objects.equals(oldItem.levelId, newItem.levelId);
    }

    public interface LevelClickCallback {
        void onClick(Level level);
    }
}
