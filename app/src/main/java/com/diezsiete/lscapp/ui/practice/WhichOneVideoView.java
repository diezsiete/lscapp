package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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

    protected ViewDataBinding createPracticeContentView() {
        PracticeWhichOneVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_which_one_video, this, false, dataBindingComponent);

        RecyclerView gridView = binding.gridView;
        PracticeWithData practice = practiceViewModel.getCurrentPracticeWithData();


        WhichOneVideoOptionAdapter rvAdapter = new WhichOneVideoOptionAdapter(dataBindingComponent,
                optionIndex -> {
                    List<Integer> answerUserList = new ArrayList<>();
                    answerUserList.add(optionIndex);
                    practiceViewModel.setAnswerUser(answerUserList);
                });

        rvAdapter.replace(practice.getWords());
        gridView.setAdapter(rvAdapter);


        binding.setPractice(practice);

        //gridView.setSelector(R.drawable.selector_button);


        return binding;
    }

}
