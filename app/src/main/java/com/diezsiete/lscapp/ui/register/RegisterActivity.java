package com.diezsiete.lscapp.ui.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.DataManagerResponse;
import com.diezsiete.lscapp.data.db.model.User;
import com.diezsiete.lscapp.ui.base.BaseActivity;
import com.diezsiete.lscapp.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity implements RegisterContract.MvpView {

    @Inject
    RegisterContract.Presenter<RegisterContract.MvpView> mPresenter;

    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.password) TextInputEditText mPasswordView;
    @BindView(R.id.password_confirm) TextInputEditText mPasswordConfirmView;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }


    @Override
    protected void setUp() {
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return !mPresenter.onEnterEmail(mEmailView.getText().toString());
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return !mPresenter.onEnterPassword(mPasswordView.getText().toString());
            }
        });
        mPasswordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                boolean ok = mPresenter.onEnterPasswordConfirm(
                    mPasswordView.getText().toString(), mPasswordConfirmView.getText().toString());

                if (ok && id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    mPresenter.onServerRegisterClick(mEmailView.getText().toString(),
                            mPasswordView.getText().toString(), mPasswordConfirmView.getText().toString());
                }
                return ok;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setUnBinder(ButterKnife.bind(this));

        setUp();
    }

    @OnClick(R.id.button_submit)
    void onServerRegisterClick(View v) {
        mPresenter.onServerRegisterClick(mEmailView.getText().toString(),
                mPasswordView.getText().toString(), mPasswordConfirmView.getText().toString());
    }


    @Override
    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void errorEmailValidation(@StringRes int resId) {
        mEmailView.setError(getString(resId));
        mEmailView.requestFocus();
    }

    @Override
    public void errorPasswordValidation(@StringRes int resId) {
        mPasswordView.setError(getString(resId));
        mPasswordView.requestFocus();
    }

    @Override
    public void errorPasswordConfirmValidation(@StringRes int resId) {
        mPasswordConfirmView.setError(getString(resId));
        mPasswordConfirmView.requestFocus();
    }

    @Override
    public void cleanPasswordInputs() {
        mPasswordView.setText("");
        mPasswordConfirmView.setText("");
    }

    @Override
    public void resetErrors() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }
}
