package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterViewFlipper;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageBinding;

import javax.inject.Inject;


@SuppressLint("ViewConstructor")
public class DiscoverImageView extends FrameLayout {
    protected PracticeViewModel practiceViewModel;

    protected DataBindingComponent dataBindingComponent;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected LayoutInflater layoutInflater;

    PracticeDiscoverImageBinding binding;

    private int position = 0;

    public DiscoverImageView(Fragment fragment) {
        super(fragment.getContext());

        dataBindingComponent = new FragmentDataBindingComponent(fragment);
        practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);

        layoutInflater = LayoutInflater.from(fragment.getContext());
        binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image, this, true, dataBindingComponent);


        AdapterViewFlipper flipper = binding.discoverImageFlipper;

        DiscoverImageAdapter adapter = new DiscoverImageAdapter(fragment);
        flipper.setAdapter(adapter);
        flipper.setInAnimation(fragment.getContext(), R.animator.practice_right_in);
        flipper.setOutAnimation(fragment.getContext(), R.animator.practice_left_out);

        binding.setDiscoverImageView(this);
        binding.setTextId(R.string.practice_discover_image_order);
        binding.setPosition(position);

        practiceViewModel.getCurrentPractice().observe(fragment, practiceWithData -> {
            if(practiceWithData != null)
                binding.setPractice(practiceWithData);
        });
        practiceViewModel.startNewPractice();
    }


    public void onClickSubmitAnswer() {
        if(position == 0){
            position++;
            binding.setTextId(R.string.practice_discover_image_question);
            binding.discoverImageFlipper.showNext();
            binding.setPosition(position);
        }else{
            practiceViewModel.saveAnswer();
        }
    }


}
