package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.TextureView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.databinding.PracticeTakePictureBinding;
import com.diezsiete.lscapp.util.SignCameraHelper;


@SuppressLint("ViewConstructor")
public class TakePictureView extends PracticeView {

    SignCameraHelper cameraHelper;

    public TakePictureView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeTakePictureBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_take_picture, this, false);

        cameraHelper = new SignCameraHelper((Activity) getContext(), binding.texture, binding.btnTakepicture);

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
