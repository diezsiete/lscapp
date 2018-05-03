package com.diezsiete.lscapp.ui.activity;

import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.ui.base.BaseContract;

public interface LessonContract {
    interface MvpView extends BaseContract.MvpView {
        void updatePractices(Practice[] practice);
    }
    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>{
        void onViewPrepared();
    }
}
