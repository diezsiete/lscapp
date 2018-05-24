package com.diezsiete.lscapp.ui.level;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;


import com.diezsiete.lscapp.repository.LevelRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class LevelViewModel extends ViewModel{

    private final LiveData<Resource<List<Level>>> results;
    final MutableLiveData<String> levelId;
    private final LiveData<Resource<Level>> level;


    @Inject
    LevelViewModel(LevelRepository levelRepository) {
        results = levelRepository.loadLevels();

        this.levelId = new MutableLiveData<>();
        level = Transformations.switchMap(levelId, input -> {
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            }
            return levelRepository.loadLevel(input);
        });

    }

    public LiveData<Resource<List<Level>>> getResults() {
        return results;
    }

    public LiveData<Resource<Level>> getLevel() {
        return level;
    }

    public void setId(String levelId) {
        MutableLiveData<String> update = new MutableLiveData<>();
        update.setValue(levelId);
        if(this.levelId.equals(update))
            return;
        this.levelId.setValue(levelId);
    }

    public void retry() {

    }
}
