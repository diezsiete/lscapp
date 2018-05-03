package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.PracticePresenter;

import javax.inject.Inject;

public class TakePicturePracticePresenter<V extends TakePicturePracticeContract.SubMvpView> extends PracticePresenter<V>
        implements TakePicturePracticeContract.Presenter<V> {

    @Inject
    public TakePicturePracticePresenter(DataManager dataManager) {
        super(dataManager);
    }
}
