package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.ShowSign;
import com.diezsiete.lscapp.vo.Word;

import java.util.List;


@SuppressLint("ViewConstructor")
public class ShowSignView extends PracticeView {
    public ShowSignView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeShowSignBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_show_sign, this, false, dataBindingComponent);


        PracticeWithData practice = practiceViewModel.getCurrentPracticeWithData();
        binding.setPractice(practice);

        return binding;
    }
}
