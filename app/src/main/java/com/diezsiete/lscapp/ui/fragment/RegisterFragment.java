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
import android.view.inputmethod.EditorInfo;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentLevelSelectionBinding;
import com.diezsiete.lscapp.databinding.FragmentLoginBinding;
import com.diezsiete.lscapp.databinding.FragmentRegisterBinding;
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
public class RegisterFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentRegisterBinding> binding;

    private UserViewModel userViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentRegisterBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_register, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);


        userViewModel.getUserValidation().observe(this, userValidation -> {
            if(userValidation != null)
                binding.get().setUserValidation(userValidation);
        });
        
        userViewModel.getAuthenticatedUser().observe(this, userResource -> {
            if(userResource != null && userResource.data != null){
                navigationController.navigateToLevelSelection();
            }
        });

        binding.get().setRegisterFragment(this);

        binding.get().email.setOnEditorActionListener((textView, i, keyEvent) ->
             !userViewModel.setEmail(textView.getText().toString())
        );
        binding.get().password.setOnEditorActionListener((textView, i, keyEvent) ->
            !userViewModel.setPassword(textView.getText().toString())
        );
        binding.get().passwordConfirm.setOnEditorActionListener(((textView, i, keyEvent) -> {
            return !clickRegisterButton();
            /*boolean ok = userViewModel.setPasswordConfirm(textView.getText().toString());
            if (ok && i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_NULL)
                ok = clickRegisterButton();
            return !ok;*/
        }));
    }

    public boolean clickRegisterButton() {
        String email = binding.get().email.getText().toString();
        String password = binding.get().password.getText().toString();
        String passwordConfirm = binding.get().passwordConfirm.getText().toString();
        return userViewModel.register(email, password, passwordConfirm);
    }

    public void clickLoginButton() {
        navigationController.navigateToLogin();
    }
}
