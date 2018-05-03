package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.PracticePresenter;

import javax.inject.Inject;

public class DiscoverImagePracticePresenter<V extends DiscoverImagePracticeContract.SubMvpView> extends PracticePresenter<V>
        implements DiscoverImagePracticeContract.Presenter<V> {

    @Inject
    public DiscoverImagePracticePresenter(DataManager dataManager) {
        super(dataManager);
    }
}
