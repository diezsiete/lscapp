package com.diezsiete.lscapp.ui.practice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.diezsiete.lscapp.repository.LessonRepository;
import com.diezsiete.lscapp.repository.PracticeRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.vo.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PracticeViewModel extends ViewModel {
    //private final LiveData<Resource<List<Practice>>> practices;
    private final LiveData<List<String>> practicesCodes;
    private final LiveData<Resource<List<PracticeWithData>>> practicesWithData;
    private final MutableLiveData<String> lessonId = new MutableLiveData<>();
    private final MutableLiveData<String> goToLevel = new MutableLiveData<>();

    private int currentPracticeIndex = 0;

    private final MutableLiveData<Boolean> showNext = new MutableLiveData<>();

    private LessonRepository lessonRepository;


    @Inject
    PracticeViewModel(PracticeRepository practiceRepository, LessonRepository lessonRepository) {
        /*practices = Transformations.switchMap(lessonId, id -> {
            if (id.isEmpty()) {
                return AbsentLiveData.create();
            }else {
                return practiceRepository.loadPracticesByLessonId(id);
            }
        });*/

        practicesWithData = Transformations.switchMap(lessonId, id -> {
            if (id.isEmpty()) {
                return AbsentLiveData.create();
            }else {
                return practiceRepository.loadPracticesWithDataByLessonId(id);
            }
        });

        practicesCodes = Transformations.switchMap(practicesWithData, practices -> {
            if(practices == null || practices.data == null || practices.data.size() == 0)
                return AbsentLiveData.create();
            else{
                List<Integer> ids = new ArrayList<>();
                for(PracticeWithData practice : practices.data) {
                    ids.add(practice.practice.id);
                }
                return practiceRepository.getPracticesCodes(ids);
            }
        });



        this.lessonRepository = lessonRepository;
    }

    /*public LiveData<Resource<List<Practice>>> getPractices() {
        return practices;
    }*/

    public LiveData<List<String>> getPracticesCodes() {
        return practicesCodes;
    }

    public LiveData<Resource<List<PracticeWithData>>> getPracticesWithData() {
        return practicesWithData;
    }


    @SuppressWarnings("ConstantConditions")
    public PracticeWithData getCurrentPracticeWithData() {
        return practicesWithData.getValue().data.get(currentPracticeIndex);
    }

    public void setId(String lessonId) {
        MutableLiveData<String> update = new MutableLiveData<>();
        update.setValue(lessonId);
        if(this.lessonId.equals(update))
            return;
        this.lessonId.setValue(lessonId);
    }

    public void setShowNext() {
        int practicesSize = practicesCodes.getValue().size();
        if(currentPracticeIndex < practicesSize) {
            currentPracticeIndex++;
            int progressBarPos = currentPracticeIndex * 100 / practicesCodes.getValue().size();
            lessonRepository.updateLessonProgress(this.lessonId.getValue(), progressBarPos);
            if(currentPracticeIndex < practicesSize)
                this.showNext.setValue(true);
        }
    }

    public LiveData<Boolean> showNext() {
        return showNext;
    }

    public LiveData<String> goToLevel() {
        return goToLevel;
    }
    public void goToLevel(String levelId) {
        goToLevel.setValue(levelId);
    }
}
