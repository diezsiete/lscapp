package com.diezsiete.lscapp.ui.activity;

import android.support.annotation.StringRes;

import com.diezsiete.lscapp.ui.base.BaseContract;

public interface RegisterContract {
    interface MvpView extends BaseContract.MvpView {
        void openMainActivity();

        void errorEmailValidation(@StringRes int resId);

        void errorPasswordValidation(@StringRes int resId);

        void errorPasswordConfirmValidation(@StringRes int resId);

        void cleanPasswordInputs();

        void resetErrors();
    }
    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>{
        boolean onEnterEmail(String email);

        boolean onEnterPassword(String password);

        boolean onEnterPasswordConfirm(String password, String passwordConfirm);

        void onServerRegisterClick(String email, String password, String passwordConfirm);
    }
}
