/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diezsiete.lscapp.widget.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.ImageGridAdapter;
import com.diezsiete.lscapp.model.Image;
import com.diezsiete.lscapp.model.practice.DiscoverImagePractice;
import com.diezsiete.lscapp.model.practice.TakePicturePractice;
import com.squareup.picasso.Picasso;


@SuppressLint("ViewConstructor")
public class DiscoverImagePracticeView extends AbsPracticeView<DiscoverImagePractice> {

    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

    static {
        LAYOUT_PARAMS.gravity = Gravity.CENTER;
    }

    ImageGridAdapter mAdapter;
    RecyclerView mGridView;
    public DiscoverImagePracticeView(Context context, DiscoverImagePractice practice) {
        super(context, practice);
    }


    @Override
    protected View createPracticeContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.practice_discover_image, this, false);
        //ImageView imageView = container.findViewById(R.id.iv_image);
        //Picasso.with(getContext()).load(getPractice().getImages()[0]).into(imageView);

        mAdapter = new ImageGridAdapter(getContext());
        mGridView = (RecyclerView) container.findViewById(R.id.grid_view);
        mGridView.setHasFixedSize(true);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mGridView.setLayoutManager(sglm);
        mGridView.setAdapter(mAdapter);

        getImages();

        return container;
    }

    private void getImages() {
        Image[] images = getPractice().getImages();

        for(Image image : images) {
            image.setWidth(150);
            image.setHeight((int) (50 + Math.random() * 450));

            mAdapter.addDrawable(image);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected boolean isAnswerCorrect() { return true; }

    @Override
    public Bundle getUserInput() {
        return new Bundle();
    }

    @Override
    public void setUserInput(Bundle savedInput) {
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }



}
