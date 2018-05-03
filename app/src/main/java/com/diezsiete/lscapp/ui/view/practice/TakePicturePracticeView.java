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

package com.diezsiete.lscapp.ui.view.practice;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.di.component.ActivityComponent;
import com.diezsiete.lscapp.ui.base.BasePracticeView;
import com.diezsiete.lscapp.utils.SignCameraHelper;

import javax.inject.Inject;


@SuppressLint("ViewConstructor")
public class TakePicturePracticeView extends BasePracticeView implements TakePicturePracticeContract.SubMvpView {
    @Inject
    TakePicturePracticeContract.Presenter<TakePicturePracticeContract.SubMvpView> mPresenter;

    private static final String TAG = "TakePicturePracticeView";

    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
    static {
        LAYOUT_PARAMS.gravity = Gravity.CENTER;
    }

    @Override
    protected void submitAnswer() {
        mPresenter.onClickButtonSubmitAnswer();
    }

    public TakePicturePracticeView(Context context, Practice practice) {
        super(context, practice);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);
        }
        mPresenter.onViewPrepared(practice);
    }

    @Override
    protected void setUpQuestionView() {
        mQuestionView.setText(R.string.practice_take_picture_question);
    }


    SignCameraHelper mCameraHelper;

    @Override
    protected View createPracticeContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.practice_take_picture, this, false);


        TextureView textureView = (TextureView) container.findViewById(R.id.texture);
        Button takePictureButton = (Button) container.findViewById(R.id.btn_takepicture);
        mCameraHelper = new SignCameraHelper((Activity) getContext(), textureView, takePictureButton);


        return container;
    }

    @Override
    protected boolean isAnswerCorrect() {return true;}
    @Override
    public Bundle getUserInput() {
        return new Bundle();
    }
    @Override
    public void setUserInput(Bundle savedInput) {}
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Log.e(TAG, "onPause");
        mCameraHelper.stop();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Log.e(TAG, "onResume");
        mCameraHelper.start();
    }

    @Override
    protected void setUp() {

    }
}
