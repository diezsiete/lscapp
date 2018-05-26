package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.PracticeContainerBinding;
import com.diezsiete.lscapp.ui.fragment.LessonFragment;
import com.diezsiete.lscapp.ui.view.signvideo.SignVideoManager;
import com.diezsiete.lscapp.viewmodel.PracticeViewModel;
import com.diezsiete.lscapp.db.entity.Practice;

@SuppressLint("ViewConstructor")
public abstract class PracticeView extends FrameLayout{
    protected PracticeViewModel practiceViewModel;

    protected DataBindingComponent dataBindingComponent;

    protected LiveData<Practice> practiceLiveData;

    //@Inject
    //ViewModelProvider.Factory viewModelFactory;

    protected LayoutInflater layoutInflater;

    protected Fragment fragment;

    SignVideoManager videoManager;

    private PracticeContainerBinding containerBinding;

    public PracticeView(Fragment fragment) {
        super(fragment.getContext());

        this.fragment = fragment;

        dataBindingComponent = new FragmentDataBindingComponent(fragment);
        //practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);

        practiceViewModel = ViewModelProviders.of(fragment, ((LessonFragment) fragment).practiceViewModelFactory).get(PracticeViewModel.class);
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
            containerBinding.setPractice(practice);});
    }

    protected abstract ViewDataBinding createPracticeContentView();


    protected void setPracticeQuestion(String question){
        containerBinding.setQuestion(question);
    }

    public void onClickSubmitAnswer() {
        practiceViewModel.saveAnswer();
    }

    public boolean enableSave(@Nullable Practice practice){
        return practice == null || practice.getEnableSave();
    }

    protected void addPracticeObserver(PracticeObserver observer){
        practiceLiveData.observe(fragment, observer);
    }

    protected abstract class PracticeObserver implements Observer<Practice>{
        @Override
        public void onChanged(@Nullable Practice practice) {
            if(practice != null) {
                if(practice.getCompleted()) {
                    practiceLiveData.removeObserver(this);
                    videoManager.unsetOnSingleTapUp();
                }
                onPracticeChanged(practice);
            }
        }
        public abstract void onPracticeChanged(Practice practice);
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
