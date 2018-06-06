package com.diezsiete.lscapp.ui.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.FragmentProfileBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.ui.MainActivity;
import com.diezsiete.lscapp.ui.adapter.AchievementAdapter;
import com.diezsiete.lscapp.ui.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.viewmodel.MainActivityViewModel;
import com.diezsiete.lscapp.viewmodel.UserViewModel;
import com.diezsiete.lscapp.vo.ToolbarData;

import javax.inject.Inject;


public class ProfileFragment extends Fragment implements Injectable {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentProfileBinding> binding;


    private UserViewModel userViewModel;
    private MainActivityViewModel mainActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentProfileBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainActivityViewModel.class);


        mainActivityViewModel.setToolbarData(
                new ToolbarData(getString(R.string.profile_title), true , false));

        initAchievements();

        userViewModel.getUser().observe(this, user -> {
            if(user != null){
                binding.get().setUserEntity(user);
            }
        });
    }


    private void initAchievements() {
        RecyclerView rvAchievements = binding.get().rvAchievements;

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvAchievements.setLayoutManager(layoutManager);

        AchievementAdapter rvAdapter = new AchievementAdapter(dataBindingComponent,null);
        rvAchievements.setAdapter(rvAdapter);
        AutoClearedValue<AchievementAdapter> adapter = new AutoClearedValue<>(this, rvAdapter);

        userViewModel.getAchievements().observe(this, result -> {
            AchievementAdapter aAdapter = adapter.get();
            if(aAdapter != null)
                aAdapter.replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }


}
