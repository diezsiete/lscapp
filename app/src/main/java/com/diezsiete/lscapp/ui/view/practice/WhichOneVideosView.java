package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeWhichOneVideosBinding;
import com.diezsiete.lscapp.ui.adapter.WhichOneVideosAdapter;
import com.diezsiete.lscapp.ui.view.signvideo.SignVideoManager;
import com.diezsiete.lscapp.db.entity.Practice;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewConstructor")
public class WhichOneVideosView extends PracticeView {

    public WhichOneVideosView(Fragment fragment) {
        super(fragment);
    }

    private boolean practiceCompleted = false;

    protected ViewDataBinding createPracticeContentView() {
        PracticeWhichOneVideosBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_which_one_videos, this, false);

        RecyclerView gridView = binding.gridView;


        WhichOneVideosAdapter rvAdapter = new WhichOneVideosAdapter(dataBindingComponent,
                optionIndex -> {
                    if(!practiceCompleted) {
                        List<Integer> answerUserList = new ArrayList<>();
                        answerUserList.add(optionIndex);
                        practiceViewModel.setAnswerUser(answerUserList);
                    }
                });
        gridView.setAdapter(rvAdapter);


        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(Practice practice) {
                binding.setPractice(practice);
                if(practice.getAnswerUser() == null){
                    rvAdapter.replace(practice.getVideos());
                    setPracticeQuestion(practice.getQuestion());
                }
                if(practice.getCompleted()){
                    practiceCompleted = true;
                    videoManager.unsetOnSingleTapUp();
                    if(!practice.getAnswerCorrect())
                        videoManager.getSignVideo(practice.getAnswer()).play();
                }
            }
        });


        videoManager.unsetOnSingleTapUp().setOnSingleTapUp(new SignVideoManager.onSingleTapUpListener() {
            @Override
            public void onSingleTapUp(int position, boolean playing) {
                if(playing) {
                    List<Integer> answerUserList = new ArrayList<>();
                    answerUserList.add(position);
                    practiceViewModel.setAnswerUser(answerUserList);
                }
            }
        });


        return binding;
    }
}
