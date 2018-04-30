package com.diezsiete.lscapp.ui.login;

import com.diezsiete.lscapp.ui.base.BaseContract;

public interface LoginContract extends BaseContract{
    interface MvpView extends BaseContract.MvpView {
        void openMainActivity();
        void openRegisterActivity();
    }
    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>{
        void onRegisterClick();
        void onServerLoginClick(String email, String password);

    }
}
