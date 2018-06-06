package com.diezsiete.lscapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.diezsiete.lscapp.vo.ToolbarData;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> showBackButton;

    private final MutableLiveData<String> goToLevel = new MutableLiveData<>();

    private final MutableLiveData<ToolbarData> toolbarData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> drawerData = new MutableLiveData<>();
    private final MutableLiveData<Integer> menuItemSelected = new MutableLiveData<>();

    private final MutableLiveData<ToastData> toast = new MutableLiveData<>();

    private ToolbarData mainToolbar;

    @Inject
    MainActivityViewModel() {
        showBackButton = new MutableLiveData<>();
        showBackButton.setValue(false);

        mainToolbar = new ToolbarData();
        toolbarData.setValue(mainToolbar);
    }


    public void goToLevel(String levelId){
        if(levelId != null && !levelId.isEmpty())
            this.goToLevel.setValue(levelId);
    }

    public LiveData<String> goToLevel() {
        return this.goToLevel;
    }

    public LiveData<ToolbarData> getToolbarData() {
        return this.toolbarData;
    }
    public LiveData<Boolean> getDrawerData() {
        return this.drawerData;
    }
    public LiveData<Integer> getMenuItemSelected() {
        return this.menuItemSelected;
    }

    
    public void setToolbarData(ToolbarData toolbarData){
        mainToolbar = toolbarData;
        this.toolbarData.setValue(toolbarData);
    }

    public void lockDrawer(){
        drawerData.setValue(true);
    }

    public void unlockDrawer(){
        drawerData.setValue(false);
    }

    public void setMenuItemSelected(int index){
        menuItemSelected.setValue(index);
    }

    public LiveData<ToastData> getToast(){
        return toast;
    }
    public void setToast(String message) {
        if(toast.getValue() == null)
            toast.setValue(new ToastData(message));
        else{
            ToastData toastData = toast.getValue();
            toastData.message = message;
            toast.setValue(toastData);
        }
    }

    public class ToastData {
        public boolean lengthShort = true;
        public String message;

        ToastData(String message) {
            this.message = message;
        }
    }

}
