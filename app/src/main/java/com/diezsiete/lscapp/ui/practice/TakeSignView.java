package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeTakeSignBinding;
import com.diezsiete.lscapp.ui.MainActivity;
import com.diezsiete.lscapp.ui.view.signcamera.SignCameraManager;
import com.diezsiete.lscapp.vo.PracticeWithData;


@SuppressLint("ViewConstructor")
public class TakeSignView extends PracticeView {

    private SignCameraManager signCameraManager;

    public TakeSignView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeTakeSignBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_take_sign, this, false, dataBindingComponent);
        binding.setTakeSignView(this);

        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(PracticeWithData practice) {
                binding.setPractice(practice);
                setPracticeQuestion(practice.getQuestion());
            }
        });

        signCameraManager = ((MainActivity) fragment.getActivity()).getSignCameraManager();
        signCameraManager.setListener(foto -> {
            practiceViewModel.postCntk(foto);
        });

        return binding;
    }

    public void onClickTakePhoto() {
        signCameraManager.takePicture();
    }
}
