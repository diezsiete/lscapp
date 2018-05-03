package com.diezsiete.lscapp.ui.fragment;

import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.ui.base.BaseContract;

public interface LevelSelectionContract {
    interface MvpView extends BaseContract.MvpView {
        void updateLevels(Level[] levels);

        void openLevelActivity();
    }
    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>{
        void onViewPrepared();

        void onLevelClicked(Level level);
    }
}
