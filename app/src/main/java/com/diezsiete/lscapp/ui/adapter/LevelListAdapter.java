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
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.ItemLevelBinding;
import com.diezsiete.lscapp.db.entity.LevelEntity;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link LevelEntity} class.
 */
public class LevelListAdapter extends DataBoundListAdapter<LevelEntity, ItemLevelBinding> {
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
        binding.btnGotoLevel.setOnClickListener(v -> {
            LevelEntity levelEntity = binding.getLevelEntity();
            if (levelEntity != null)
                levelClickCallback.onClick(levelEntity);
        });

        return binding;
    }

    @Override
    protected void bind(ItemLevelBinding binding, LevelEntity item, int position) {
        binding.setLevelEntity(item);
    }

    @Override
    protected boolean areItemsTheSame(LevelEntity oldItem, LevelEntity newItem) {
        return Objects.equals(oldItem.levelId, newItem.levelId);
    }

    @Override
    protected boolean areContentsTheSame(LevelEntity oldItem, LevelEntity newItem) {
        return Objects.equals(oldItem.levelId, newItem.levelId);
    }

    public interface LevelClickCallback {
        void onClick(LevelEntity levelEntity);
    }
}
