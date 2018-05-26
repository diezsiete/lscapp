package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.AdapterViewFlipper;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageBinding;
import com.diezsiete.lscapp.ui.adapter.DiscoverImageAdapter;
import com.diezsiete.lscapp.db.entity.Practice;


@SuppressLint("ViewConstructor")
public class DiscoverImageView extends PracticeView {

    private int position = 0;

    PracticeDiscoverImageBinding binding;

    public DiscoverImageView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image, this, false, dataBindingComponent);

        AdapterViewFlipper flipper = binding.discoverImageFlipper;
        DiscoverImageAdapter adapter = new DiscoverImageAdapter(fragment);
        flipper.setAdapter(adapter);
        flipper.setInAnimation(fragment.getContext(), R.animator.practice_right_in);
        flipper.setOutAnimation(fragment.getContext(), R.animator.practice_left_out);

        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(Practice practice) {
                String question = fragment.getContext().getString(
                    position == 0 ? R.string.practice_discover_image_question1 : R.string.practice_discover_image_question2);
                setPracticeQuestion(question);
            }
        });
        return binding;
    }

    public void onClickSubmitAnswer() {
        if(position == 0){
            position++;
            setPracticeQuestion(fragment.getContext().getString(R.string.practice_discover_image_question2));
            binding.discoverImageFlipper.showNext();
        }else{
            practiceViewModel.saveAnswer();
        }
    }

    public boolean enableSave(@Nullable Practice practice){
        if(position == 0)
            return true;
        return practice == null || practice.getEnableSave();
    }
}
