package com.diezsiete.lscapp.ui.register;

import android.text.TextUtils;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.BasePresenter;
import com.diezsiete.lscapp.ui.login.LoginContract;

import javax.inject.Inject;

public class RegisterPresenter <V extends RegisterContract.MvpView> extends BasePresenter<V>
        implements RegisterContract.Presenter<V> {

    @Inject
    public RegisterPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public boolean onEnterEmail(String email) {
        return validateEmail(email);
    }

    @Override
    public boolean onEnterPassword(String password) {
        return validatePassword(password);
    }

    @Override
    public boolean onEnterPasswordConfirm(String password, String passwordConfirm) {
        boolean ok = validatePasswordConfirm(passwordConfirm);
        if(ok)
            ok = validatePasswordMatch(password, passwordConfirm);
        return ok;
    }

    @Override
    public void onServerRegisterClick(String email, String password, String passwordConfirm) {
        getMvpView().resetErrors();
        boolean ok = validateEmail(email);
        if(ok)
            ok = validatePassword(password);
        if(ok)
            ok = validatePasswordConfirm(passwordConfirm);
        if(ok)
            ok = validatePasswordMatch(password, passwordConfirm);

        if(ok)
            getMvpView().openMainActivity();
    }

    private boolean validateEmail(String email) {
        boolean ok = true;
        if (TextUtils.isEmpty(email)) {
            getMvpView().errorEmailValidation(R.string.error_field_required);
            ok = false;
        }
        else if (!email.contains("@")) {
            getMvpView().errorEmailValidation(R.string.error_invalid_email);
            ok = false;
        }
        return ok;
    }

    private boolean validatePassword(String password) {
        boolean ok = !TextUtils.isEmpty(password);
        if(!ok)
            getMvpView().errorPasswordValidation(R.string.error_field_required);
        return ok;
    }

    private boolean validatePasswordConfirm(String password) {
        boolean ok = !TextUtils.isEmpty(password);
        if(!ok)
            getMvpView().errorPasswordConfirmValidation(R.string.error_field_required);
        return ok;
    }

    private boolean validatePasswordMatch(String password, String passwordConfirm) {
        boolean ok = passwordConfirm.equals(password);
        if(!ok) {
            getMvpView().cleanPasswordInputs();
            getMvpView().errorPasswordValidation(R.string.error_password_mismatch);
        }
        return ok;
    }
}
