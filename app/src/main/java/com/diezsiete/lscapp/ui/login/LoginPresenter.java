package com.diezsiete.lscapp.ui.login;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.BasePresenter;

import javax.inject.Inject;

public class LoginPresenter<V extends LoginContract.MvpView> extends BasePresenter<V>
        implements LoginContract.Presenter<V> {

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onRegisterClick() {
        getMvpView().openRegisterActivity();
    }

    @Override
    public void onServerLoginClick(String email, String password) {

    }
}
