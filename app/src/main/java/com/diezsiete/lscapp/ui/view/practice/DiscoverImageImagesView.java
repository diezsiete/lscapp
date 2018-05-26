package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.PracticeDiscoverImageImagesBinding;
import com.diezsiete.lscapp.ui.adapter.DiscoverImageGridAdapter;
import com.diezsiete.lscapp.vo.Picture;
import com.diezsiete.lscapp.db.entity.Practice;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewConstructor")
public class DiscoverImageImagesView extends PracticeView {

    public DiscoverImageImagesView(Fragment fragment) {
        super(fragment);
    }

    private boolean practiceCompleted = false;

    @Override
    protected void bind() {
        PracticeDiscoverImageImagesBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.practice_discover_image_images, this, true);

        DiscoverImageGridAdapter adapter = new DiscoverImageGridAdapter(getContext(), optionIndex -> {
            if(!practiceCompleted) {
                List<Integer> answerUserList = new ArrayList<>();
                answerUserList.add(optionIndex);
                practiceViewModel.setAnswerUser(answerUserList);
            }
        });

        RecyclerView gridView = binding.gridView;
        gridView.setHasFixedSize(true);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridView.setLayoutManager(sglm);
        gridView.setAdapter(adapter);

        addPracticeObserver(new PracticeObserver() {
            @Override
            public void onPracticeChanged(Practice practice) {
                binding.setPractice(practice);

                if(practice.getAnswerUser() == null) {
                    for(Picture picture : practice.getPictures()) {
                        picture.width = 150;
                        picture.height = (int) (50 + Math.random() * 450);
                        adapter.addDrawable(picture);
                    }
                    adapter.notifyDataSetChanged();
                }
                if(practice.getCompleted())
                    practiceCompleted = true;
            }
        });
    }

    @Override
    protected ViewDataBinding createPracticeContentView() {
        return null;
    }
}
