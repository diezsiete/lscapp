package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeWhichOneVideosBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewConstructor")
public class WhichOneVideosView extends PracticeView {

    public WhichOneVideosView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeWhichOneVideosBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_which_one_videos, this, false);

        RecyclerView gridView = binding.gridView;
        PracticeWithData practice = practiceViewModel.getCurrentPracticeWithData();

        WhichOneVideosAdapter rvAdapter = new WhichOneVideosAdapter(dataBindingComponent,
                optionIndex -> {
                    List<Integer> answerUserList = new ArrayList<>();
                    answerUserList.add(optionIndex);
                    practiceViewModel.setAnswerUser(answerUserList);
                });

        rvAdapter.replace(practice.getVideos());
        gridView.setAdapter(rvAdapter);


        return binding;
    }
}
