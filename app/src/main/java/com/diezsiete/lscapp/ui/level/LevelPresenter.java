package com.diezsiete.lscapp.ui.level;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.DataManagerResponse;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.ui.base.BasePresenter;

import javax.inject.Inject;

public class LevelPresenter<V extends LevelContract.MvpView> extends BasePresenter<V>
        implements LevelContract.Presenter<V> {

    @Inject
    public LevelPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {
        getMvpView().showLoading();
        getDataManager().getLevels(new DataManagerResponse<Level[]>(){
            @Override
            public void onResponse(Level[] response) {
                getMvpView().hideLoading();
                getMvpView().updateLevels(response);
            }
            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideLoading();
                getMvpView().showMessage(R.string.error_message);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onLevelClicked(Level level) {

    }
}
