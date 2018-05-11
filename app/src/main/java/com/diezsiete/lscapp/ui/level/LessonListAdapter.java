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

package com.diezsiete.lscapp.ui.level;



import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.ItemLessonBinding;
import com.diezsiete.lscapp.ui.common.DataBoundListAdapter;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link Level} class.
 */
public class LessonListAdapter extends DataBoundListAdapter<Lesson, ItemLessonBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final LessonClickCallback lessonClickCallback;

    public LessonListAdapter(DataBindingComponent dataBindingComponent, LessonClickCallback lessonClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.lessonClickCallback = lessonClickCallback;
    }

    @Override
    protected ItemLessonBinding createBinding(ViewGroup parent) {
        ItemLessonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lesson,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Lesson lesson = binding.getLesson();
            if (lesson != null && lessonClickCallback != null) {
                lessonClickCallback.onClick(lesson);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemLessonBinding binding, Lesson item, int position) {
        binding.setLesson(item);
    }

    @Override
    protected boolean areItemsTheSame(Lesson oldItem, Lesson newItem) {
        return Objects.equals(oldItem.lessonId, newItem.lessonId);
    }

    @Override
    protected boolean areContentsTheSame(Lesson oldItem, Lesson newItem) {
        return Objects.equals(oldItem.levelId, newItem.levelId);
    }

    public interface LessonClickCallback {
        void onClick(Lesson lesson);
    }
}
