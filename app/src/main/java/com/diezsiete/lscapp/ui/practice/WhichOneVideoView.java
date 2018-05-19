package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeWhichOneVideoBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewConstructor")
public class WhichOneVideoView extends PracticeView {


    public WhichOneVideoView(Fragment fragment) {
        super(fragment);
    }

    private boolean practiceCompleted = false;

    protected ViewDataBinding createPracticeContentView() {
        PracticeWhichOneVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_which_one_video, this, false, dataBindingComponent);

        RecyclerView gridView = binding.gridView;

        WhichOneVideoOptionAdapter.ClickCallback clickCallback = new WhichOneVideoOptionAdapter.ClickCallback() {
            @Override
            public void onClick(Integer optionIndex) {
                if(!practiceCompleted) {
                    List<Integer> answerUserList = new ArrayList<>();
                    answerUserList.add(optionIndex);
                    practiceViewModel.setAnswerUser(answerUserList);
                }
            }
        };
        WhichOneVideoOptionAdapter rvAdapter = new WhichOneVideoOptionAdapter(dataBindingComponent, clickCallback);


        gridView.setAdapter(rvAdapter);

        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(PracticeWithData practice) {
                binding.setPractice(practice);
                if(practice.getAnswerUser() == null) {
                    setPracticeQuestion(practice.getQuestion());
                    binding.setVideoUrl(practice.getVideo());
                    rvAdapter.replace(practice.getWords());
                }if(practice.getCompleted()) {
                    practiceCompleted = true;
                }
            }
        });

        return binding;
    }

}
