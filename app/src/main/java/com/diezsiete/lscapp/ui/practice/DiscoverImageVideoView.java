package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageVideoBinding;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;

import javax.inject.Inject;

@SuppressLint("ViewConstructor")
public class DiscoverImageVideoView extends FrameLayout {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public DiscoverImageVideoView(Fragment fragment) {
        super(fragment.getContext());
        DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(fragment);
        LayoutInflater layoutInflater = LayoutInflater.from(fragment.getContext());

        PracticeDiscoverImageVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image_video, this, true, dataBindingComponent);

        PracticeViewModel practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);

        practiceViewModel.getCurrentPractice().observe(fragment, practiceWithData -> {
            if(practiceWithData != null)
                binding.setPractice(practiceWithData);
        });

    }
}
