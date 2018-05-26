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
import android.view.TextureView;

import com.diezsiete.lscapp.util.AppConstants;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class SignCameraManager implements LifecycleObserver {
    private final static String TAG = "SignCameraManager";

    public final static int PERMISSION_DENIED_STATUS_PERMANENTLY = 1;
    public final static int PERMISSION_DENIED_STATUS_TEMPORALLY = 2;

    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private boolean mPermissionsGranted = false;
    private boolean mPermissionsAsked = false;
    private int mPermissionDeniedStatus;

    private SignCamera signCamera;
    private Activity activity;

    private SignCameraListener listener;


    public interface SignCameraListener {
        public void onPhotoTaken(File file);
        public void onPermissionDenied();
        public void onPermissionGranted();
    }

    public SignCameraManager(Activity activity, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.activity = activity;
    }

    public void startSignCamera(){
        if(signCamera != null) {
            if(!mPermissionsAsked) {
                mPermissionsGranted = hasAllPermissions(activity, PERMISSIONS);
                if (!mPermissionsGranted) {
                    ActivityCompat.requestPermissions(activity, PERMISSIONS, AppConstants.CAMERA_REQUEST_PERMISSION);
                }
            }

            if(mPermissionsGranted) {
                signCamera.start();
                if(listener != null)
                    listener.onPermissionGranted();
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

    public void callOnPermissionDenied(Activity activity){
        mPermissionsAsked = true;
        mPermissionDeniedStatus = checkPermissionsDeniedTemporally(activity);
        if(listener != null)
            listener.onPermissionDenied();
    }

    public void callOnPermissionGranted(){
        mPermissionsAsked = true;
        mPermissionsGranted = true;
        if(listener != null)
            listener.onPermissionGranted();
    }

    public int getPermissionDeniedStatus() {
        return mPermissionDeniedStatus;
    }
    public SignCameraManager resetPermissionAsked() {
        mPermissionsAsked = false;
        return this;
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

    private int checkPermissionsDeniedTemporally(Activity activity){
        boolean allTemporally = true;
        for(String permission : PERMISSIONS){
            if (!hasAllPermissions(activity, permission) && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                allTemporally = false;
            }
        }
        return allTemporally ? PERMISSION_DENIED_STATUS_TEMPORALLY : PERMISSION_DENIED_STATUS_PERMANENTLY;
    }
}
