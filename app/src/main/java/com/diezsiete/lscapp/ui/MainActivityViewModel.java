package com.diezsiete.lscapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> showBackButton;

    private final MutableLiveData<String> goToLevel = new MutableLiveData<>();

    @Inject
    MainActivityViewModel() {
        showBackButton = new MutableLiveData<>();
        showBackButton.setValue(false);
    }

    public MutableLiveData<Boolean> getShowBackButton() {
        return showBackButton;
    }

    public void setShowBackButton(Boolean show) {
        this.showBackButton.setValue(show);
    }


    public void goToLevel(String levelId){
        if (Objects.equals(this.goToLevel.getValue(), levelId))
            return;
        this.goToLevel.setValue(levelId);
    }

    public LiveData<String> goToLevel() {
        return this.goToLevel;
    }

}
