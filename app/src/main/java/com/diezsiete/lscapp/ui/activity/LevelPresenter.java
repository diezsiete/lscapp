package com.diezsiete.lscapp.ui.activity;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.DataManagerResponse;
import com.diezsiete.lscapp.data.db.model.Lesson;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.ui.base.BasePresenter;

import javax.inject.Inject;

public class LevelPresenter <V extends LevelContract.MvpView> extends BasePresenter<V>
        implements LevelContract.Presenter<V> {

    @Inject
    public LevelPresenter(DataManager dataManager) {
        super(dataManager);
    }

    private void getLessons(String levelId) {
        getDataManager().getLessonsByLevelId(levelId, new DataManagerResponse<Lesson[]>() {
            @Override
            public void onResponse(Lesson[] response) {
                getMvpView().updateLessons(response);
                getMvpView().hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideLoading();
            }
        });
    }

    @Override
    public void onViewPrepared() {
        getMvpView().showLoading();
        getDataManager().getCurrentLevel(new DataManagerResponse<Level>() {
            @Override
            public void onResponse(Level response) {
                getMvpView().setLevel(response);
                getLessons(response.getLevelId());
            }
            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideLoading();
            }
        });

    }

    @Override
    public void onLessonClicked(Lesson lesson) {
        getDataManager().setCurrentLesson(lesson);
        getMvpView().openLessonActivity();
    }
}