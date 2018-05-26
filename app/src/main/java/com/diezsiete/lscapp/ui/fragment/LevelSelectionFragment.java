package com.diezsiete.lscapp.ui.fragment;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentLevelSelectionBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.viewmodel.MainActivityViewModel;
import com.diezsiete.lscapp.ui.adapter.LevelListAdapter;
import com.diezsiete.lscapp.ui.NavigationController;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.viewmodel.LevelViewModel;

import javax.inject.Inject;

/**
 *
 */
public class LevelSelectionFragment extends Fragment implements Injectable {

    @Inject
    public ViewModelProvider.Factory levelViewModelFactory;
    @Inject
    public ViewModelProvider.Factory mainActivityViewModelFactory;

    @Inject
    public NavigationController navigationController;

    public DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

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

        levelViewModel = ViewModelProviders.of(this, levelViewModelFactory).get(LevelViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(getActivity(), mainActivityViewModelFactory).get(MainActivityViewModel.class);

        initRecyclerView();
        LevelListAdapter rvAdapter = new LevelListAdapter(dataBindingComponent,
                level -> navigationController.navigateToLevel(level.levelId));


        binding.get().levels.setAdapter(rvAdapter);

        adapter = new AutoClearedValue<>(this, rvAdapter);

        mainActivityViewModel.setShowBackButton(false);

        mainActivityViewModel.setToolbarData(getString(R.string.sign_practice),
            "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)), "");

        binding.get().setCallback(() -> levelViewModel.retry());

        mainActivityViewModel.unlockDrawer();
        mainActivityViewModel.setMenuItemSelected(0);
    }

    private void initRecyclerView() {
        levelViewModel.getResults().observe(this, result -> {
            binding.get().setResource(result);
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }
}
