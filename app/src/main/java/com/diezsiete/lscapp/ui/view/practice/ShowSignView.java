package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.db.entity.Practice;


@SuppressLint("ViewConstructor")
public class ShowSignView extends PracticeView {
    public ShowSignView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeShowSignBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_show_sign, this, false, dataBindingComponent);


        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(Practice practice) {
                binding.setPractice(practice);
            }
        });

        return binding;
    }
}
