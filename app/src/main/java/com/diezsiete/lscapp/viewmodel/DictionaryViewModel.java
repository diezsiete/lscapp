package com.diezsiete.lscapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;


import com.diezsiete.lscapp.repository.WordRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.List;

import javax.inject.Inject;

public class DictionaryViewModel extends ViewModel {
    private final LiveData<Resource<WordEntity>> word;
    private final LiveData<Resource<List<WordEntity>>> words;
    private final MutableLiveData<String> wordSelected = new MutableLiveData<>();

    @Inject
    DictionaryViewModel(WordRepository wordRepository) {

        words = wordRepository.loadWords();

        word = Transformations.switchMap(wordSelected, wordSel -> {
            if (wordSel.isEmpty()) {
                return AbsentLiveData.create();
            }
            return wordRepository.loadWord(wordSel);
        });
    }

    public LiveData<Resource<List<WordEntity>>> getWords() {
        return words;
    }

    public LiveData<Resource<WordEntity>> getWord() {
        return word;
    }

    public void setWord(String word) {
        wordSelected.setValue(word);
    }

    public void unsetWord() {
        wordSelected.setValue("");
    }
}
