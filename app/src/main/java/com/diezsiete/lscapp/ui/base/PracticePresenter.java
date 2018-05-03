package com.diezsiete.lscapp.ui.base;


import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.ui.base.BasePresenter;
import com.diezsiete.lscapp.ui.base.PracticeContract;

import javax.inject.Inject;

public class PracticePresenter <V extends PracticeContract.SubMvpView> extends BasePresenter<V>
        implements PracticeContract.Presenter<V> {

    protected Practice mPractice;

    @Inject
    public PracticePresenter(DataManager dataManager) {
        super(dataManager);

    }

    @Override
    public void onViewPrepared(Practice practice) {
        mPractice = practice;
    }

    @Override
    public void onClickButtonSubmitAnswer() {
        getMvpView().showNextPractice();
    }
}
