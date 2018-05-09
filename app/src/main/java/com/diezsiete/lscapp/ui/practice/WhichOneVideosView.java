package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.widget.GridView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeWhichOneVideosBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;


@SuppressLint("ViewConstructor")
public class WhichOneVideosView extends PracticeView {

    public WhichOneVideosView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeWhichOneVideosBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_which_one_videos, this, false);

        GridView gridView = binding.gridView;
        PracticeWithData practice = practiceViewModel.getCurrentPracticeWithData();

        gridView.setSelector(R.drawable.selector_button);
        /*gridView.setAdapter(new WhichOneVideosAdapter(practice.videos, R.layout.item_video));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/
        return binding;
    }
}
