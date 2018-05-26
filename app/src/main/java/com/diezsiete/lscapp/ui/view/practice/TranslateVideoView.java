package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeTranslateVideoBinding;
import com.diezsiete.lscapp.ui.view.wordselector.WordSelector;
import com.diezsiete.lscapp.db.entity.Practice;


@SuppressLint("ViewConstructor")
public class TranslateVideoView extends PracticeView {

    public TranslateVideoView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeTranslateVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_translate_video, this, false, dataBindingComponent);

        WordSelector wordSelector = new WordSelector(binding.wordSelectorRoot, options -> {
            practiceViewModel.setAnswerUser(options);
        });

        addPracticeObserver(new PracticeObserver() {
            private boolean setted = false;
            @Override
            public void onPracticeChanged(Practice practice) {
                if(!setted) {
                    binding.setPractice(practice);
                    wordSelector.setOptions(practice.getWords().get(0));
                    setted = true;
                    setPracticeQuestion(practice.getQuestion());
                }
            }
        });

        return binding;
    }
}
