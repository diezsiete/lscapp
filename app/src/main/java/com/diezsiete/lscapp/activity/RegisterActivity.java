package com.diezsiete.lscapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
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


public class RegisterActivity extends AppCompatActivity {

    AutoCompleteTextView mEmailView;
    EditText mPasswordView;
    EditText mPasswordConfirmView;
    Button mSubmitView;

    private View mProgressView;
    private View mLoginFormView;

    private Object mAuthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = findViewById(R.id.email);
        mPasswordView  = findViewById(R.id.password);
        mPasswordConfirmView = findViewById(R.id.password_confirm);
        mSubmitView = findViewById(R.id.button_submit);


        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            return !validateEmail();
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            return !validatePassword();
            }
        });

        mPasswordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptRegister();
                return true;
            }
            return false;
            }
        });

        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean ok = validateEmail();
        if(ok)
            ok = validatePassword();
        if(ok)
            ok = validatePasswordConfirm();

        if(ok){
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            executeRegister(
                mEmailView.getText().toString(),
                mPasswordView.getText().toString(),
                mPasswordConfirmView.getText().toString()
            );
        }
    }

    private boolean validateEmail() {
        String email = mEmailView.getText().toString();
        boolean ok = true;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            ok = false;
        }
        else if (!email.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            ok = false;
        }
        if(!ok)
            mEmailView.requestFocus();
        return ok;
    }

    private boolean validatePassword() {
        String password = mPasswordView.getText().toString();
        boolean ok = true;
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            ok = false;
        }
        if(!ok)
            mPasswordView.requestFocus();
        return ok;
    }

    private boolean validatePasswordConfirm() {
        String passwordConfirm = mPasswordConfirmView.getText().toString();
        boolean ok = true;
        if(TextUtils.isEmpty(passwordConfirm)) {
            mPasswordConfirmView.setError(getString(R.string.error_field_required));
            mPasswordConfirmView.requestFocus();
            ok = false;
        }else if(!passwordConfirm.equals(mPasswordView.getText().toString())){
            mPasswordView.setText("");
            mPasswordConfirmView.setText("");
            mPasswordView.setError(getString(R.string.error_password_mismatch));
            mPasswordView.requestFocus();
            ok = false;
        }
        return ok;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void executeRegister(String email, String password, String passwordConfirm) {
        DataManager.createUser(email, password, passwordConfirm, new DataManagerResponse<User>() {
            @Override
            public void onResponse(User response) {
                Toast.makeText(RegisterActivity.this, "Yeah user id : " + response.getProfileId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                /*
                switch(response.code()){
                    case 404:
                        try {
                            Toast.makeText(RegisterActivity.this, "Error 404 : " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(RegisterActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        Toast.makeText(RegisterActivity.this, "Error 500", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RegisterActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

}
