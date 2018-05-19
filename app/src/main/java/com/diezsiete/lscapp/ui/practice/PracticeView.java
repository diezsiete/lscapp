package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.PracticeContainerBinding;
import com.diezsiete.lscapp.ui.lesson.LessonFragment;
import com.diezsiete.lscapp.util.signvideo.SignVideoManager;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.PracticeWithData;


import javax.inject.Inject;

@SuppressLint("ViewConstructor")
public abstract class PracticeView extends FrameLayout{
    protected PracticeViewModel practiceViewModel;

    protected DataBindingComponent dataBindingComponent;

    protected LiveData<PracticeWithData> practiceLiveData;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected LayoutInflater layoutInflater;

    protected Fragment fragment;

    SignVideoManager videoManager;

    private PracticeContainerBinding containerBinding;

    public PracticeView(Fragment fragment) {
        super(fragment.getContext());

        this.fragment = fragment;

        dataBindingComponent = new FragmentDataBindingComponent(fragment);
        practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);
        layoutInflater = LayoutInflater.from(fragment.getContext());

        videoManager = ((LessonFragment) fragment).videoManager;

        practiceLiveData = practiceViewModel.getCurrentPractice();

        bind();
    }

    protected void bind(){
        containerBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_container, this, true, dataBindingComponent);
        containerBinding.setPracticeView(this);

        containerBinding.practiceViewContainer.addView(createPracticeContentView().getRoot());

        practiceLiveData.observe(fragment, practice -> {
            containerBinding.setPracticeWithData(practice);});
    }

    protected abstract ViewDataBinding createPracticeContentView();


    protected void setPracticeQuestion(String question){
        containerBinding.setQuestion(question);
    }

    public void onClickSubmitAnswer() {
        practiceViewModel.saveAnswer();
    }

    public boolean enableSave(@Nullable PracticeWithData practice){
        return practice == null || practice.getEnableSave();
    }

    protected void addPracticeObserver(PracticeObserver observer){
        practiceLiveData.observe(fragment, observer);
    }

    protected abstract class PracticeObserver implements Observer<PracticeWithData>{
        @Override
        public void onChanged(@Nullable PracticeWithData practice) {
            if(practice != null) {
                if(practice.getCompleted()) {
                    practiceLiveData.removeObserver(this);
                    videoManager.unsetOnSingleTapUp();
                }
                onPracticeChanged(practice);
            }
        }
        public abstract void onPracticeChanged(PracticeWithData practice);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
