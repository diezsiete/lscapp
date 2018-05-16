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
import com.diezsiete.lscapp.databinding.PracticeContainerBinding;
import com.diezsiete.lscapp.vo.Lesson;


import javax.inject.Inject;

@SuppressLint("ViewConstructor")
public abstract class PracticeView extends FrameLayout{
    protected PracticeViewModel practiceViewModel;

    protected DataBindingComponent dataBindingComponent;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected LayoutInflater layoutInflater;

    protected Fragment fragment;

    public PracticeView(Fragment fragment) {
        super(fragment.getContext());

        this.fragment = fragment;

        dataBindingComponent = new FragmentDataBindingComponent(fragment);
        practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);
        layoutInflater = LayoutInflater.from(fragment.getContext());

        PracticeContainerBinding binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.practice_container, this, true, dataBindingComponent);
        binding.setViewmodel(practiceViewModel);


        binding.practiceViewContainer.addView(createPracticeContentView().getRoot());

        practiceViewModel.getCurrentPractice().observe(fragment, practiceWithData -> {
            if(practiceWithData != null) {
                binding.setPracticeWithData(practiceWithData);
            }
        });
        practiceViewModel.startNewPractice();
    }

    protected abstract ViewDataBinding createPracticeContentView();

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
