package com.diezsiete.lscapp.ui.base;


import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.di.component.ActivityComponent;

import butterknife.Unbinder;

public abstract class BaseFrameLayout extends FrameLayout implements BaseContract.SubMvpView {

    protected BaseActivity mActivity;
    private Unbinder mUnBinder;

    public BaseFrameLayout(Context context) {
        super(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }


    @Override
    public void showLoading() {
        if (mActivity != null) {
            mActivity.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mActivity != null) {
            mActivity.hideLoading();
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    protected abstract void setUp();
}
