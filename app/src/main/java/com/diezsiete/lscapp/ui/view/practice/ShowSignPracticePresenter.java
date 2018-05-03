package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.ui.base.PracticePresenter;

import javax.inject.Inject;

public class ShowSignPracticePresenter <V extends ShowSignPracticeContract.SubMvpView> extends PracticePresenter<V>
        implements ShowSignPracticeContract.Presenter<V> {

    @Inject
    public ShowSignPracticePresenter(DataManager dataManager) {
        super(dataManager);
    }
}
