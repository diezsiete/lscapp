package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.PracticePresenter;

import javax.inject.Inject;

public class WhichOneVideosPracticePresenter<V extends WhichOneVideosPracticeContract.SubMvpView> extends PracticePresenter<V>
        implements WhichOneVideosPracticeContract.Presenter<V> {

    @Inject
    public WhichOneVideosPracticePresenter(DataManager dataManager) {
        super(dataManager);
    }
}
