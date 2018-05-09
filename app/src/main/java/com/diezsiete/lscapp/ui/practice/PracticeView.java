package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeContainerBinding;


import javax.inject.Inject;

@SuppressLint("ViewConstructor")
public abstract class PracticeView extends FrameLayout{
    protected PracticeViewModel practiceViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected LayoutInflater layoutInflater;

    public PracticeView(Fragment fragment) {
        super(fragment.getContext());
        practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);

        layoutInflater = LayoutInflater.from(fragment.getContext());
        PracticeContainerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.practice_container, this, true);
        binding.setPracticeWithData(practiceViewModel.getCurrentPracticeWithData());
        binding.setViewmodel(practiceViewModel);

        binding.practiceViewContainer.addView(createPracticeContentView().getRoot(), 1);

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
