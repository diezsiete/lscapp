package com.diezsiete.lscapp.ui.view.practice;

import com.diezsiete.lscapp.ui.base.PracticeContract;

public interface WhichOneVideosPracticeContract {
    public interface SubMvpView extends PracticeContract.SubMvpView {

    }

    public interface Presenter<V extends SubMvpView> extends PracticeContract.Presenter<V> {

    }
}
