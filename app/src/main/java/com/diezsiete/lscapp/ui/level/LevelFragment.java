

package com.diezsiete.lscapp.ui.level;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentLevelBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.common.NavigationController;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import javax.inject.Inject;

/**
 * El controlador UI para mostrar información del Nivel con sus lecciones
 */
public class LevelFragment extends Fragment implements Injectable {

    private static final String LEVEL_ID_KEY = "levelId";

    @Inject
    public ViewModelProvider.Factory levelViewModelFactory;
    @Inject
    public ViewModelProvider.Factory lessonViewModelFactory;
    @Inject
    public ViewModelProvider.Factory mainActivityViewModelFactory;

    @Inject
    public NavigationController navigationController;

    public DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLevelBinding> binding;

    AutoClearedValue<LessonListAdapter> adapter;

    private LevelViewModel levelViewModel;
    private LessonViewModel lessonViewModel;
    private MainActivityViewModel mainActivityViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLevelBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_level, container, false, dataBindingComponent);
        //dataBinding.setRetryCallback(() -> repoViewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        levelViewModel = ViewModelProviders.of(this, levelViewModelFactory).get(LevelViewModel.class);
        lessonViewModel = ViewModelProviders.of(this, lessonViewModelFactory).get(LessonViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(getActivity(), mainActivityViewModelFactory).get(MainActivityViewModel.class);

        Bundle args = getArguments();
        if (args != null && args.containsKey(LEVEL_ID_KEY)) {
            levelViewModel.setId(args.getString(LEVEL_ID_KEY));
            lessonViewModel.setId(args.getString(LEVEL_ID_KEY));
        } else {
            levelViewModel.setId(null);
            lessonViewModel.setId(null);
        }

        levelViewModel.getLevel().observe(this, resource -> {
            binding.get().setLevel(resource == null ? null : resource.data);
            if(resource != null && resource.data != null)
                mainActivityViewModel.setToolbarData(resource.data.name, resource.data.color, resource.data.image);
            // this is only necessary because espresso cannot read data binding callbacks.
            binding.get().executePendingBindings();
        });


        LessonListAdapter rvAdapter = new LessonListAdapter(dataBindingComponent,
                lesson -> navigationController.navigateToLesson(lesson.lessonId));
        binding.get().lessons.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        mainActivityViewModel.setShowBackButton(true);

        binding.get().setCallback(() -> lessonViewModel.retry());

        initLessonsList();
    }

    private void initLessonsList() {
        lessonViewModel.getResults().observe(this, result -> {
            binding.get().setResource(result);
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }

    public static LevelFragment create(String levelId) {
        LevelFragment levelFragment = new LevelFragment();
        Bundle args = new Bundle();
        args.putString(LEVEL_ID_KEY, levelId);
        levelFragment.setArguments(args);
        return levelFragment;
    }
}
