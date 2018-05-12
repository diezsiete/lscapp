package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

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

        cameraHelper = new SignCameraHelper(
                (Activity) getContext(), binding.texture, binding.btnTakepicture, foto -> {
                    practiceViewModel.postCntk(foto);
        });

        return binding;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Log.e(TAG, "onPause");
        cameraHelper.stop();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Log.e(TAG, "onResume");
        cameraHelper.start();
    }
}
