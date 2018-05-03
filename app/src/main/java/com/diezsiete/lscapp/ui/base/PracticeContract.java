package com.diezsiete.lscapp.ui.base;

import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.ui.base.BaseContract;

public interface PracticeContract {
    public interface SubMvpView extends BaseContract.SubMvpView {
        void showNextPractice();
    }

    public interface Presenter<V extends SubMvpView> extends BaseContract.Presenter<V>{
        void onViewPrepared(Practice practice);
        void onClickButtonSubmitAnswer();
    }
}
