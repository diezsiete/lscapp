package com.diezsiete.lscapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.diezsiete.lscapp.repository.LessonRepository;
import com.diezsiete.lscapp.repository.PracticeRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.AnswerMessage;
import com.diezsiete.lscapp.db.entity.Practice;
import com.diezsiete.lscapp.vo.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PracticeViewModel extends ViewModel {
    private final MediatorLiveData<List<String>> practicesCodesMediator = new MediatorLiveData<>();
    private final MediatorLiveData<Practice> practiceMediator = new MediatorLiveData<>();
    private final List<practiceMediatorCallback> practiceMediatorCallbacks = new ArrayList<>();

    private final LiveData<Resource<List<Practice>>> practicesWithData;
    private final LiveData<Practice> practice;
    private final MutableLiveData<Integer> currentPracticeId = new MutableLiveData<>();
    private final MutableLiveData<String> lessonId = new MutableLiveData<>();
    private final MutableLiveData<String> goToLevel = new MutableLiveData<>();

    private int currentPracticeIndex = 0;


    private final MutableLiveData<AnswerMessage> answerMessage = new MutableLiveData<>();

    private final MutableLiveData<Boolean> saveAnswer = new MutableLiveData<>();

    private final MutableLiveData<Boolean> showNext = new MutableLiveData<>();

    private PracticeRepository practiceRepository;
    private LessonRepository lessonRepository;

    private List<Integer> answerUser = new ArrayList<>();

    private Practice currentPractice;

    @Inject
    PracticeViewModel(PracticeRepository practiceRepository, LessonRepository lessonRepository) {
        MediatorLiveData<String> mediator = new MediatorLiveData<>();
        mediator.addSource(lessonId, id -> {
            if(id != null && !id.isEmpty()){
                practiceRepository.deletePracticesByLessonId(id, () -> mediator.setValue(id));
            }
        });

        practicesWithData = Transformations.switchMap(mediator, id -> {
            if (id.isEmpty()) {
                return AbsentLiveData.create();
            }else {
                return practiceRepository.loadPracticesWithDataByLessonId(id);
            }
        });

        practicesCodesMediator.addSource(practicesWithData, practices -> {
            if(practices != null && practices.data != null && practices.data.size() > 0 && fetch) {
                List<String> codes = new ArrayList<>();
                for (Practice practice : practices.data)
                    codes.add(practice.entity.code);
                practicesCodesMediator.setValue(codes);

                currentPracticeId.setValue(getCurrentPracticeWithData().entity.id);
            }
        });


        practice = Transformations.switchMap(currentPracticeId, practiceId -> {
            if (practiceId == null) {
                return AbsentLiveData.create();
            }else {
                return practiceRepository.getPracticeWithData(practiceId);
            }
        });

        practiceMediator.addSource(practice, p -> {
            if(p != null){
                currentPractice = p;
                practiceMediator.setValue(p);
                for(int i = 0; i < practiceMediatorCallbacks.size(); i++)
                    practiceMediatorCallbacks.get(i).execute(p);
            }
        });

        this.practiceRepository = practiceRepository;
        this.lessonRepository = lessonRepository;
    }

    public LiveData<List<String>> getPracticesCodes() {
        //return practicesCodes;
        return practicesCodesMediator;
    }

    public LiveData<Resource<List<Practice>>> getPracticesWithData() {
        return practicesWithData;
    }

    public LiveData<AnswerMessage> answerMessage() {
        return answerMessage;
    }

    public void saveAnswer() {
        Practice practice = currentPractice;
        String practiceAnswer = practice.getStringAnswer();
        boolean ok = practice.validateAnswer();


        practiceMediatorCallbacks.add(practiceCallback -> {
            practiceMediatorCallbacks.clear();

            if(practice.entity.code.equals("show-sign")){
                setShowNext();
            }else{
                AnswerMessage message = ok ? AnswerMessage.success(practiceAnswer) : AnswerMessage.danger(practiceAnswer);
                answerMessage.setValue(message);
            }
        });
        practiceRepository.updatePractice(practice.entity);
    }


    private boolean fetch = true;

    @SuppressWarnings("ConstantConditions")
    public Practice getCurrentPracticeWithData() {
        fetch = false;
        return practicesWithData.getValue().data.get(currentPracticeIndex);
    }

    public LiveData<Practice> getCurrentPractice() {
        fetch = false;
        //return practice;
        return practiceMediator;
    }

    /**
     * @Deprecated
     */
    public void startNewPractice(){
        currentPracticeId.setValue(getCurrentPracticeWithData().entity.id);
    }

    public void setId(String lessonId) {
        MutableLiveData<String> update = new MutableLiveData<>();
        update.setValue(lessonId);
        if(this.lessonId.equals(update))
            return;
        this.lessonId.setValue(lessonId);
    }

    public void setShowNext() {
        final int practicesSize = practicesCodesMediator.getValue().size();
        if(currentPracticeIndex < practicesSize) {
            currentPracticeIndex++;

            practiceMediatorCallbacks.add(practice -> {
                practiceMediatorCallbacks.clear();
                answerMessage.setValue(null);
                int progressBarPos = currentPracticeIndex * 100 / practicesSize;
                lessonRepository.updateLessonProgress(this.lessonId.getValue(), progressBarPos);
                this.showNext.setValue(true);
            });

            if(currentPracticeIndex < practicesSize)
                currentPracticeId.setValue(getCurrentPracticeWithData().entity.id);
            else{
                int progressBarPos = currentPracticeIndex * 100 / practicesSize;
                lessonRepository.updateLessonProgress(this.lessonId.getValue(), progressBarPos);
            }
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

    public void setAnswerUser(List<Integer> answerUser) {
        getCurrentPracticeWithData().entity.answerUser = answerUser;
        practiceRepository.updatePractice(getCurrentPracticeWithData().entity);
    }

    public void postCntk(File file){
        practiceMediatorCallbacks.add(takeSignPractice -> {
            if(takeSignPractice.getAnswerUser() != 2){
                practiceMediatorCallbacks.clear();
                boolean deleted = file.delete();
                saveAnswer();
            }
        });
        practiceRepository.postCntk(practice.getValue(), getCurrentPracticeWithData().getWord(), file);
    }

    private interface practiceMediatorCallback {
        public void execute(Practice practice);
    }

}
