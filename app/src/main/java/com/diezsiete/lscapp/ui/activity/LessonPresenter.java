package com.diezsiete.lscapp.ui.activity;


import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.DataManagerResponse;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.ui.base.BasePresenter;

import javax.inject.Inject;

public class LessonPresenter <V extends LessonContract.MvpView> extends BasePresenter<V>
        implements LessonContract.Presenter<V> {

    @Inject
    public LessonPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {
        getMvpView().showLoading();
        String lessonId = getDataManager().getCurrentLessonId();
        getDataManager().getPracticesByLessonId(lessonId, new DataManagerResponse<Practice[]>() {
            @Override
            public void onResponse(Practice[] response) {
                getMvpView().hideLoading();
                getMvpView().updatePractices(response);
            }
            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideLoading();
            }
        });
    }
}
