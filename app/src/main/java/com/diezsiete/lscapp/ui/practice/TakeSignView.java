package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeTakeSignBinding;
import com.diezsiete.lscapp.ui.widget.SignCameraHelper;


@SuppressLint("ViewConstructor")
public class TakeSignView extends PracticeView {

    SignCameraHelper cameraHelper;

    public TakeSignView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeTakeSignBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_take_sign, this, false);


        practiceViewModel.getCurrentPractice().observe(fragment, practiceWithData -> {
            if(practiceWithData != null && practiceWithData.entity.code.equals("take-sign")) {
                binding.setPractice(practiceWithData);
                if(practiceWithData.getAnswerUser() != null && practiceWithData.getAnswerUser() != 2){
                    //practiceViewModel.saveAnswer();
                    //cameraHelper.rotate();
                }
            }
        });

        cameraHelper = new SignCameraHelper(
                (Activity) getContext(), binding.texture, binding.btnTakepicture, foto -> {
            practiceViewModel.postCntk(foto);
            //cameraHelper.rotate();
        });


        return binding;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cameraHelper.stop();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        cameraHelper.start();
    }
}
