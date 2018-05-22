package com.diezsiete.lscapp.ui.view.signcamera;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.TextureView;

import com.diezsiete.lscapp.util.signvideo.SignVideo;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SignCameraManager implements LifecycleObserver {
    private final static String TAG = "SignCameraManager";

    private static final int PERMISSION_ALL = 105;
    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private boolean mPermissionsGranted = false;

    private SignCamera signCamera;
    private Activity activity;


    private SignCameraListener listener;


    public interface onSingleTapUpListener {
        void onSingleTapUp(int position, boolean playing);
    }

    public SignCameraManager(Activity activity, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.activity = activity;
    }

    public void startSignCamera(){
        if(signCamera != null) {
            mPermissionsGranted = hasAllPermissions(activity, PERMISSIONS);
            if (!mPermissionsGranted) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            } else {
                signCamera.start();
            }
        }
    }

    public SignCamera getSignCamera(){
        if(signCamera == null)
            signCamera = new SignCamera(activity, file -> {
                if(listener != null)
                    listener.onPhotoTaken(file);
            });
        return signCamera;
    }

    public void setListener(SignCameraListener listener) {
        this.listener = listener;
    }

    public SignCameraManager setTexture(TextureView view) {
        getSignCamera().setTexture(view);
        return this;
    }

    public void takePicture() {
        if(signCamera != null)
            signCamera.takePicture();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        if(Util.SDK_INT > 23) {
            startSignCamera();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        if(Util.SDK_INT <= 23) {
            startSignCamera();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        if (Util.SDK_INT <= 23 && signCamera != null)
            signCamera.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        if (Util.SDK_INT > 23 && signCamera != null) {
            signCamera.stop();
        }
    }

    private static boolean hasAllPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public interface SignCameraListener {
        public void onPhotoTaken(File file);
    }
}
