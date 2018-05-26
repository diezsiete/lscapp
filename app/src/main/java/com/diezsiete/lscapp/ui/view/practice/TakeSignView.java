package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.View;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeTakeSignBinding;
import com.diezsiete.lscapp.ui.MainActivity;
import com.diezsiete.lscapp.ui.view.signcamera.SignCameraManager;
import com.diezsiete.lscapp.db.entity.Practice;

import java.io.File;


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

        binding.setPermissionGranted(true);

        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(Practice practice) {
                binding.setPractice(practice);
                setPracticeQuestion(practice.getQuestion());
            }
        });

        signCameraManager = ((MainActivity) fragment.getActivity()).getSignCameraManager();
        signCameraManager.setListener(new SignCameraManager.SignCameraListener() {
            @Override
            public void onPhotoTaken(File file) {
                practiceViewModel.postCntk(file);
            }
            @Override
            public void onPermissionDenied() {
                binding.setPermissionGranted(false);
            }
            @Override
            public void onPermissionGranted() {
                binding.setPermissionGranted(true);
            }
        });

        return binding;
    }

    public void onClickTakePhoto() {
        signCameraManager.takePicture();
    }

    public void onClickEnablePermissionCamera() {
        signCameraManager.resetPermissionAsked();
        if(signCameraManager.getPermissionDeniedStatus() == SignCameraManager.PERMISSION_DENIED_STATUS_TEMPORALLY){
            signCameraManager.startSignCamera();
        }else
            ((MainActivity) fragment.getActivity()).navigationController.navigateToPermissions();
    }

    public int buttonTakePhotoVisibility(Practice practice, boolean permissionGranted){
        if(!permissionGranted){
            return View.INVISIBLE;
        }
        if(practice == null || practice.getAnswerUser() == null || practice.getAnswerUser() == 2)
            return View.VISIBLE;

        return View.INVISIBLE;
    }
}
