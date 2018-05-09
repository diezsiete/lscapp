package com.diezsiete.lscapp.ui.level;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentLevelSelectionBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.common.LevelListAdapter;
import com.diezsiete.lscapp.ui.common.NavigationController;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.vo.Level;

import javax.inject.Inject;

/**
 *
 */
public class LevelSelectionFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLevelSelectionBinding> binding;

    AutoClearedValue<LevelListAdapter> adapter;

    private LevelViewModel levelViewModel;
    private MainActivityViewModel mainActivityViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLevelSelectionBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_level_selection, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        levelViewModel = ViewModelProviders.of(this, viewModelFactory).get(LevelViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainActivityViewModel.class);

        initRecyclerView();
        LevelListAdapter rvAdapter = new LevelListAdapter(dataBindingComponent,
                level -> navigationController.navigateToLevel(level.levelId));


        binding.get().levels.setAdapter(rvAdapter);

        adapter = new AutoClearedValue<>(this, rvAdapter);

        mainActivityViewModel.setShowBackButton(false);

        //binding.get().setCallback(() -> searchViewModel.refresh());
    }

    private void initRecyclerView() {
        levelViewModel.getResults().observe(this, result -> {
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }
}
