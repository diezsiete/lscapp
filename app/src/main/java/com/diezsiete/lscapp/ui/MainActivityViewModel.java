package com.diezsiete.lscapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.diezsiete.lscapp.vo.MainToolbarData;

import java.util.Objects;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> showBackButton;

    private final MutableLiveData<String> goToLevel = new MutableLiveData<>();

    private final MutableLiveData<MainToolbarData> toolbarData = new MutableLiveData<>();

    private final MainToolbarData mainToolbar;

    @Inject
    MainActivityViewModel() {
        showBackButton = new MutableLiveData<>();
        showBackButton.setValue(false);

        mainToolbar = new MainToolbarData();
        toolbarData.setValue(mainToolbar);
    }

    public MutableLiveData<Boolean> getShowBackButton() {
        return showBackButton;
    }

    public void setShowBackButton(Boolean show) {
        this.showBackButton.setValue(show);
    }


    public void goToLevel(String levelId){
        if(levelId != null && !levelId.isEmpty())
            this.goToLevel.setValue(levelId);
    }

    public LiveData<String> goToLevel() {
        return this.goToLevel;
    }

    public LiveData<MainToolbarData> getToolbarData() {
        return this.toolbarData;
    }

    public void setToolbarTitle(String title) {
        mainToolbar.title = title;
        toolbarData.setValue(mainToolbar);
    }

    public void setToolbarData(String title, String color) {
        mainToolbar.title = title;
        mainToolbar.color = color;
        toolbarData.setValue(mainToolbar);
    }

    public void setToolbarData(String title, String color, String image) {
        mainToolbar.title = title;
        mainToolbar.color = color;
        mainToolbar.image = image;
        toolbarData.setValue(mainToolbar);
    }

}
