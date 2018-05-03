package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.PracticePresenter;

import javax.inject.Inject;

public class TranslateVideoPracticePresenter<V extends TranslateVideoPracticeContract.SubMvpView> extends PracticePresenter<V>
        implements TranslateVideoPracticeContract.Presenter<V> {

    @Inject
    public TranslateVideoPracticePresenter(DataManager dataManager) {
        super(dataManager);
    }
}
