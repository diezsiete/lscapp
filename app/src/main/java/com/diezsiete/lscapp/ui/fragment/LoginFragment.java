package com.diezsiete.lscapp.ui.fragment;


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
import com.diezsiete.lscapp.databinding.FragmentLoginBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.common.LevelListAdapter;
import com.diezsiete.lscapp.ui.common.NavigationController;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.viewmodel.UserViewModel;
import com.diezsiete.lscapp.vo.Level;

import javax.inject.Inject;

/**
 *
 */
public class LoginFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLoginBinding> binding;


    private UserViewModel userViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLoginBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_login, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        binding.get().setLoginFragment(this);



    }

    public void clickLoginButton() {
        Log.d("JOSE", "OKAS LOGIN");
    }

    public void clickRegisterButton() {
        navigationController.navigateToRegister();
    }

}
