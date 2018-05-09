

package com.diezsiete.lscapp.ui.dictionary;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentDictionaryBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.util.SignVideoManager;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * El controlador UI para mostrar información del Nivel con sus lecciones
 */
public class DictionaryFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentDictionaryBinding> binding;

    private DictionaryViewModel dictionaryViewModel;

    AutoClearedValue<WordListAdapter> adapter;

    public SignVideoManager videoManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentDictionaryBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_dictionary, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dictionaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(DictionaryViewModel.class);
        binding.get().setViewmodel(dictionaryViewModel);
        dictionaryViewModel.getWord().observe(this, resource -> {
            binding.get().setWord(resource == null ? null : resource.data);
        });

        initLessonsList();
        WordListAdapter rvAdapter = new WordListAdapter(dataBindingComponent,
                word -> dictionaryViewModel.setWord(word));

        binding.get().dictionary.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);


        videoManager = new SignVideoManager(this.getContext(), getLifecycle());
        Log.d("JOSE", "SignVideoManagerCreated");
    }

    private void initLessonsList() {
        dictionaryViewModel.getWords().observe(this, result -> {
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }

}
