package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.PracticePresenter;

import javax.inject.Inject;

public class WhichOneVideoPracticePresenter<V extends WhichOneVideoPracticeContract.SubMvpView> extends PracticePresenter<V>
        implements WhichOneVideoPracticeContract.Presenter<V> {

    @Inject
    public WhichOneVideoPracticePresenter(DataManager dataManager) {
        super(dataManager);
    }
}
