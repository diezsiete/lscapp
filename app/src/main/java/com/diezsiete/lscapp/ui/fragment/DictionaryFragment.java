

package com.diezsiete.lscapp.ui.fragment;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.adapter.WordListAdapter;
import com.diezsiete.lscapp.ui.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.FragmentDictionaryBinding;
import com.diezsiete.lscapp.di.Injectable;
import com.diezsiete.lscapp.viewmodel.MainActivityViewModel;
import com.diezsiete.lscapp.util.AutoClearedValue;
import com.diezsiete.lscapp.ui.view.signvideo.SignVideoManager;
import com.diezsiete.lscapp.viewmodel.DictionaryViewModel;
import com.diezsiete.lscapp.vo.ToolbarData;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private MainActivityViewModel mainActivityViewModel;

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
        mainActivityViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainActivityViewModel.class);
        binding.get().setViewmodel(dictionaryViewModel);
        dictionaryViewModel.getWord().observe(this, resource -> {
            binding.get().setWordEntity(resource == null ? null : resource.data);
        });

        initLessonsList();
        WordListAdapter rvAdapter = new WordListAdapter(dataBindingComponent,
                word -> dictionaryViewModel.setWord(word));

        binding.get().dictionary.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);


        videoManager = new SignVideoManager(this.getContext(), getLifecycle());
        mainActivityViewModel.setToolbarData(new ToolbarData(
                getString(R.string.dictionary), false, true
        ));
    }

    private void initLessonsList() {
        dictionaryViewModel.getWords().observe(this, result -> {
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }

}
