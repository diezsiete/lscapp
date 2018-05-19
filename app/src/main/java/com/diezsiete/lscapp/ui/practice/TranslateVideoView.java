package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.databinding.PracticeTranslateVideoBinding;
import com.diezsiete.lscapp.ui.widget.wordselector.WordSelector;
import com.diezsiete.lscapp.vo.PracticeWithData;


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
            public void onPracticeChanged(PracticeWithData practice) {
                if(!setted) {
                    binding.setPractice(practice);
                    wordSelector.setOptions(practice.getWords().get(0));
                    setted = true;
                }
            }
        });

        return binding;
    }
}
