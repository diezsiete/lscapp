package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageBinding;
import com.diezsiete.lscapp.databinding.PracticeShowSignBinding;
import com.diezsiete.lscapp.vo.PracticeWithData;


@SuppressLint("ViewConstructor")
public class DiscoverImageView extends PracticeView {


    public DiscoverImageView(Fragment fragment) {
        super(fragment);
    }

    protected ViewDataBinding createPracticeContentView() {
        PracticeDiscoverImageBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image, this, false);

        PracticeWithData practice = practiceViewModel.getCurrentPracticeWithData();
        binding.setPractice(practice);

        DiscoverImageGridAdapter adapter = new DiscoverImageGridAdapter(getContext());

        RecyclerView gridView = binding.gridView;
        gridView.setHasFixedSize(true);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridView.setLayoutManager(sglm);
        gridView.setAdapter(adapter);

        List<String> = practice.getImages();
        for(Image image : images) {
            image.setWidth(150);
            image.setHeight((int) (50 + Math.random() * 450));
            mAdapter.addDrawable(image);
        }
        mAdapter.notifyDataSetChanged();

        return binding;
    }
}
