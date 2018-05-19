package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageVideoBinding;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;

import javax.inject.Inject;

@SuppressLint("ViewConstructor")
public class DiscoverImageVideoView extends PracticeView {

    public DiscoverImageVideoView(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected void bind() {
        PracticeDiscoverImageVideoBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image_video, this, true, dataBindingComponent);

        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(PracticeWithData practice) {
                if(practice.getAnswerUser() == null)
                    binding.setPractice(practice);
            }
        });
    }

    @Override
    protected ViewDataBinding createPracticeContentView() {
        return null;
    }
}
