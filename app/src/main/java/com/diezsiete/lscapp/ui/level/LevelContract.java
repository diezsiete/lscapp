package com.diezsiete.lscapp.ui.level;

import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.ui.base.BaseContract;

public interface LevelContract {
    interface MvpView extends BaseContract.MvpView {
        void updateLevels(Level[] levels);

        void openPracticeActivity(Level level);
    }
    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>{
        void onViewPrepared();

        void onLevelClicked(Level level);
    }
}
