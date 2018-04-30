package com.diezsiete.lscapp.ui.main;

import com.diezsiete.lscapp.ui.base.BaseContract;

public interface MainContract {
    interface MvpView extends BaseContract.MvpView{
        void openLoginActivity();

        void closeNavigationDrawer();

        void showLevelSelectionFragment();
    }

    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>  {
        void onDrawerOptionPracticeClick();

        void onDrawerOptionLogoutClick();

        void onDrawerDictionaryClick();

        void onViewInitialized();
    }
}
