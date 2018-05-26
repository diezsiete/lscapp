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
import com.diezsiete.lscapp.databinding.ItemWordBinding;
import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link WordEntity} class.
 */
public class WordListAdapter extends DataBoundListAdapter<WordEntity, ItemWordBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final WordClickCallback wordClickCallback;

    public WordListAdapter(DataBindingComponent dataBindingComponent, WordClickCallback wordClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.wordClickCallback = wordClickCallback;
    }

    @Override
    protected ItemWordBinding createBinding(ViewGroup parent) {
        ItemWordBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_word,
                        parent, false, dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            wordClickCallback.onClick(binding.getWordEntity().word);
        });
        return binding;
    }

    @Override
    protected void bind(ItemWordBinding binding, WordEntity item, int position) {
        binding.setWordEntity(item);
    }

    @Override
    protected boolean areItemsTheSame(WordEntity oldItem, WordEntity newItem) {
        return Objects.equals(oldItem, newItem);
    }

    @Override
    protected boolean areContentsTheSame(WordEntity oldItem, WordEntity newItem) {
        return Objects.equals(oldItem, newItem);
    }

    public interface WordClickCallback {
        void onClick(String word);
    }
}
