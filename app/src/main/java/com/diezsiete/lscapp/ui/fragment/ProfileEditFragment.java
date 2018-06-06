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
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.FragmentProfileBinding;
import com.diezsiete.lscapp.databinding.FragmentProfileEditBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.ui.MainActivity;
import com.diezsiete.lscapp.ui.NavigationController;
import com.diezsiete.lscapp.ui.adapter.AchievementAdapter;
import com.diezsiete.lscapp.ui.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.viewmodel.MainActivityViewModel;
import com.diezsiete.lscapp.viewmodel.UserViewModel;
import com.diezsiete.lscapp.vo.Status;
import com.diezsiete.lscapp.vo.ToolbarData;

import javax.inject.Inject;


public class ProfileEditFragment extends Fragment implements Injectable {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    public DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentProfileEditBinding> binding;


    private UserViewModel userViewModel;
    private MainActivityViewModel mainActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentProfileEditBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_profile_edit, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainActivityViewModel.class);


        mainActivityViewModel.setToolbarData(
                new ToolbarData(getString(R.string.profile_title), true));

        userViewModel.getUser().observe(this, user -> {
            if(user != null){
                binding.get().setUserEntity(user);
            }
        });

        userViewModel.getUserValidation().observe(this, userValidation -> {
            if(userValidation != null)
                binding.get().setUserValidation(userValidation);
        });

        binding.get().setProfileEditFragment(this);
    }


    public void clickSaveButton(){
        String name = binding.get().name.getText().toString();
        String email = binding.get().email.getText().toString();
        String password = binding.get().password.getText().toString();
        String passwordConfirm = binding.get().passwordConfirm.getText().toString();

        userViewModel.editUser(name, email, password, passwordConfirm).observe(this, userEntityResource -> {
            if(userEntityResource != null){
                String message = null;
                if(userEntityResource.status == Status.SUCCESS){
                    message = "Datos actualizados exitosamente";
                    navigationController.navigatePreviousFragment();
                }else if(userEntityResource.status == Status.ERROR){
                    message = userEntityResource.message;
                }
                if(message != null)
                    mainActivityViewModel.setToast(message);
            }
        });
    }
}
