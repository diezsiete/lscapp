package com.diezsiete.lscapp.ui.practice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageImagesBinding;
import com.diezsiete.lscapp.vo.Picture;
import com.diezsiete.lscapp.vo.PracticeWithData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@SuppressLint("ViewConstructor")
public class DiscoverImageImagesView extends FrameLayout {

    protected DataBindingComponent dataBindingComponent;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    protected LayoutInflater layoutInflater;

    public DiscoverImageImagesView(Fragment fragment) {
        super(fragment.getContext());
        layoutInflater = LayoutInflater.from(fragment.getContext());

        PracticeDiscoverImageImagesBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image_images, this, true);

        PracticeViewModel practiceViewModel = ViewModelProviders.of(fragment, viewModelFactory).get(PracticeViewModel.class);

        PracticeWithData practice = practiceViewModel.getCurrentPracticeWithData();

        DiscoverImageGridAdapter adapter = new DiscoverImageGridAdapter(getContext(), optionIndex -> {
            List<Integer> answerUserList = new ArrayList<>();
            answerUserList.add(optionIndex);
            practiceViewModel.setAnswerUser(answerUserList);
            Log.d("JOSE", "optionIndex : " + optionIndex);
        });

        RecyclerView gridView = binding.gridView;
        gridView.setHasFixedSize(true);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridView.setLayoutManager(sglm);
        gridView.setAdapter(adapter);

        for(Picture picture : practice.getPictures()) {
            picture.width = 150;
            picture.height = (int) (50 + Math.random() * 450);
            adapter.addDrawable(picture);
        }
        adapter.notifyDataSetChanged();
    }

}
