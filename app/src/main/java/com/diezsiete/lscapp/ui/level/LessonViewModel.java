package com.diezsiete.lscapp.ui.level;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diezsiete.lscapp.repository.LessonRepository;
import com.diezsiete.lscapp.repository.LevelRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class LessonViewModel extends ViewModel{
    private final LiveData<Resource<List<Lesson>>> results;
    private final MutableLiveData<String> levelId = new MutableLiveData<>();
    private final LiveData<Lesson> lesson;
    private final MutableLiveData<String> lessonId = new MutableLiveData<>();
    private MediatorLiveData<Lesson> lessonNoProgress = new MediatorLiveData<>();

    @Inject
    LessonViewModel(LessonRepository lessonRepository) {
        results = Transformations.switchMap(levelId, id -> {
            if (id.isEmpty()) {
                return AbsentLiveData.create();
            }else {
                return lessonRepository.loadLessonsByLevelId(id);
            }
        });

        lesson = Transformations.switchMap(lessonId, id -> {
            if (id.isEmpty()){
                return AbsentLiveData.create();
            }else {
                return lessonRepository.getLesson(id);
            }
        });


        lessonNoProgress.addSource(lesson, new Observer<Lesson>() {
            private boolean progressCleaned = false;
            @Override
            public void onChanged(@Nullable Lesson lesson) {
                if(lesson != null){
                    if(!progressCleaned){
                        progressCleaned = true;
                        lesson.progress = 0;
                        lessonRepository.update(lesson);
                    }
                    lessonNoProgress.setValue(lesson);
                }
            }
        });


    }

    public LiveData<Resource<List<Lesson>>> getResults() {
        return results;
    }

    public LiveData<Lesson> getLessonNoProgress() {
        return lessonNoProgress;
    }

    public void setId(String levelId) {
        MutableLiveData<String> update = new MutableLiveData<>();
        update.setValue(levelId);
        if(this.levelId.equals(update))
            return;
        this.levelId.setValue(levelId);
    }

    public void setLessonId(String lessonId) {
        if (Objects.equals(this.lessonId.getValue(), lessonId)) {
            return;
        }
        this.lessonId.setValue(lessonId);
    }

    public void retry() {

    }

}
