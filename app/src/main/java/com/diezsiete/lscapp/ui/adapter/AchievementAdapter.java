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
import com.diezsiete.lscapp.databinding.ItemAchievementBinding;
import com.diezsiete.lscapp.db.entity.AchievementEntity;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link LevelEntity} class.
 */
public class AchievementAdapter extends DataBoundListAdapter<AchievementEntity, ItemAchievementBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final AchievementClickCallback achievementClickCallback;

    public AchievementAdapter(DataBindingComponent dataBindingComponent, AchievementClickCallback achievementClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.achievementClickCallback = achievementClickCallback;
    }

    @Override
    protected ItemAchievementBinding createBinding(ViewGroup parent) {
        ItemAchievementBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_achievement,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            AchievementEntity entity = binding.getAchievementEntity();
            if (entity != null && achievementClickCallback != null) {
                achievementClickCallback.onClick(entity);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemAchievementBinding binding, AchievementEntity item, int position) {
        binding.setAchievementEntity(item);
    }

    @Override
    protected boolean areItemsTheSame(AchievementEntity oldItem, AchievementEntity newItem) {
        return Objects.equals(oldItem.title, newItem.title);
    }

    @Override
    protected boolean areContentsTheSame(AchievementEntity oldItem, AchievementEntity newItem) {
        return Objects.equals(oldItem.title, newItem.title);
    }

    public interface AchievementClickCallback {
        void onClick(AchievementEntity achievementEntity);
    }
}
