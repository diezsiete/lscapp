package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.ShowSign;
import com.diezsiete.lscapp.vo.WhichOneVideo;
import com.diezsiete.lscapp.vo.WhichOneVideos;

import java.util.ArrayList;
import java.util.List;

/**
 * @Deprecated
 */
public abstract class BoundPracticeCode {
    private final MediatorLiveData<List<PracticeWithData>> result = new MediatorLiveData<>();

    @MainThread
    BoundPracticeCode() {
        //result.setValue(new Practice("", ""));
        LiveData<List<PracticeWithData>> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            List<PracticeWithData> newList = new ArrayList<>();
            for(PracticeWithData practiceWithData : data)
                newList.add(instanceByCode(practiceWithData));
            result.setValue(newList);
        });
    }

    @NonNull
    @MainThread
    protected abstract LiveData<List<PracticeWithData>> loadFromDb();

    public LiveData<List<PracticeWithData>> asLiveData() {
        return result;
    }

    private PracticeWithData instanceByCode(PracticeWithData oldPracticeWithData){
        switch (oldPracticeWithData.practice.code){
            case "show-sign" :
                return new ShowSign(oldPracticeWithData);
            case "which-one-video" :
                return new WhichOneVideo(oldPracticeWithData);
            case "which-one-videos" :
                return new WhichOneVideos(oldPracticeWithData);
            default:
                return oldPracticeWithData;
        }
    }
}
