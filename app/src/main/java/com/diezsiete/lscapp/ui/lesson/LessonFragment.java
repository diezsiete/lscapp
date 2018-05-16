package com.diezsiete.lscapp.ui.lesson;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingComponent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentLessonBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.level.LessonViewModel;
import com.diezsiete.lscapp.ui.practice.PracticeAdapter;
import com.diezsiete.lscapp.ui.practice.PracticeViewModel;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.util.signvideo.SignVideoManager;

import javax.inject.Inject;

public class LessonFragment extends Fragment implements Injectable {
    private static final String LESSON_ID_KEY = "lessonId";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLessonBinding> binding;
    AutoClearedValue<PracticeAdapter> adapter;

    private LessonViewModel lessonViewModel;
    private PracticeViewModel practiceViewModel;
    private MainActivityViewModel mainActivityViewModel;

    private Interpolator mInterpolator;
    private ObjectAnimator mProgressBarAnimator;

    public SignVideoManager videoManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLessonBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_lesson, container, false, dataBindingComponent);
        //dataBinding.setRetryCallback(() -> repoViewModel.retry());

        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainActivityViewModel.class);
        lessonViewModel = ViewModelProviders.of(this, viewModelFactory).get(LessonViewModel.class);
        practiceViewModel = ViewModelProviders.of(this, viewModelFactory).get(PracticeViewModel.class);

        binding.get().setViewmodel(practiceViewModel);

        Bundle args = getArguments();
        if (args != null && args.containsKey(LESSON_ID_KEY)) {
            lessonViewModel.setLessonId(args.getString(LESSON_ID_KEY));
            practiceViewModel.setId(args.getString(LESSON_ID_KEY));
        } else {
            lessonViewModel.setLessonId(null);
            practiceViewModel.setId(null);
        }

        initPracticesList();

        PracticeAdapter adapter = new PracticeAdapter(getContext(), this);
        binding.get().practiceView.setAdapter(adapter);
        this.adapter = new AutoClearedValue<>(this, adapter);


        lessonViewModel.getLessonNoProgress().observe(this, lesson -> {
            binding.get().setLesson(lesson);
            if(lesson != null)
                mainActivityViewModel.setToolbarData(lesson.name, lesson.color, lesson.image);
        });

        practiceViewModel.answerMessage().observe(this, answerMessage -> {
            binding.get().setAnswerMessage(answerMessage);
        });

        practiceViewModel.showNext().observe(this, result -> {
            binding.get().practiceView.showNext();
        });

        practiceViewModel.goToLevel().observe(this, result -> {
            mainActivityViewModel.goToLevel(result);
        });

        videoManager = new SignVideoManager(this.getContext(), getLifecycle());
        initAnimations();
    }

    private void initPracticesList() {
        practiceViewModel.getPracticesCodes().observe(this, codes -> {
            if(codes != null && codes.size() > 0){
                Log.d("JOSE", "codes " + codes.size());
                adapter.get().setData(codes);
            }else
                Log.d("JOSE", "codes null");
        });
    }

    private void initAnimations() {
        binding.get().practiceView.setInAnimation(this.getContext(), R.animator.practice_right_in);
        binding.get().practiceView.setOutAnimation(this.getContext(), R.animator.practice_left_out);
    }

    public static LessonFragment create(String lessonId) {
        LessonFragment lessonFragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putString(LESSON_ID_KEY, lessonId);
        lessonFragment.setArguments(args);
        return lessonFragment;
    }

}
