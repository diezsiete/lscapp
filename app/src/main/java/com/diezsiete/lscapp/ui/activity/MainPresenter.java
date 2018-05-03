package com.diezsiete.lscapp.ui.activity;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.BasePresenter;

import javax.inject.Inject;

public class MainPresenter<V extends MainContract.MvpView> extends BasePresenter<V>
        implements MainContract.Presenter<V> {

    @Inject
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onDrawerOptionPracticeClick() {

    }

    @Override
    public void onDrawerOptionLogoutClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().openLoginActivity();
    }

    @Override
    public void onDrawerDictionaryClick() {

    }

    @Override
    public void onViewInitialized() {
        getMvpView().showLevelSelectionFragment();
    }
}
