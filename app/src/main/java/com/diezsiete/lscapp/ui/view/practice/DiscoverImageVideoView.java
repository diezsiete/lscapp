package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageVideoBinding;
import com.diezsiete.lscapp.db.entity.Practice;

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
            public void onPracticeChanged(Practice practice) {
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
