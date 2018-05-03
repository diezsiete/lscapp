package com.diezsiete.lscapp.ui.base;

import android.support.annotation.StringRes;

public interface BaseContract {
    interface MvpView {
        void showLoading();

        void hideLoading();

        void onError(@StringRes int resId);

        void onError(String message);

        void showMessage(String message);

        void showMessage(@StringRes int resId);

        void openActivityOnTokenExpire();
    }

    interface Presenter<V extends MvpView>  {
        void onAttach(V mvpView);

        void onDetach();

        //void handleApiError(ANError error);

        void setUserAsLoggedOut();
    }

    interface SubMvpView extends MvpView {

    }
}
