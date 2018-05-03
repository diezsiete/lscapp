package com.diezsiete.lscapp.ui.activity;

import com.diezsiete.lscapp.data.db.model.Lesson;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.ui.base.BaseContract;

public interface LevelContract {
    interface MvpView extends BaseContract.MvpView {
        void updateLessons(Lesson[] lessons);

        void openLessonActivity();

        void setLevel(Level level);
    }
    interface Presenter<V extends MvpView> extends BaseContract.Presenter<V>{
        void onViewPrepared();

        void onLessonClicked(Lesson leson);
    }
}
