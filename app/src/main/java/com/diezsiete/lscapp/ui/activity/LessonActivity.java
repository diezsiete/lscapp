package com.diezsiete.lscapp.ui.activity;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterViewAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.ui.adapter.PracticeAdapter;
import com.diezsiete.lscapp.ui.base.BaseChildActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LessonActivity extends BaseChildActivity implements LessonContract.MvpView {

    @Inject
    LessonContract.Presenter<LessonContract.MvpView> mPresenter;
    @Inject
    PracticeAdapter mAdapter;

    @BindView(R.id.practice_view) AdapterViewAnimator mPracticeView;
    @BindView(R.id.progress) ProgressBar mProgressBar;
    @BindView(R.id.success_icon) ImageView mLessonEndIcon;
    @BindView(R.id.practice_end_button) Button mLessonEndButton;

    @OnClick(R.id.practice_end_button)
    public void buttonEndClick() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private Interpolator mInterpolator;
    private ObjectAnimator mProgressBarAnimator;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LessonActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setUnBinder(ButterKnife.bind(this));

        setUp();
    }

    @Override
    protected void setUp() {
        mInterpolator = new AccelerateInterpolator();

        mProgressBarAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 0);
        mProgressBarAnimator.setDuration(600);
        mProgressBarAnimator.setInterpolator(mInterpolator);

        mPracticeView.setInAnimation(this, R.animator.practice_right_in);
        mPracticeView.setOutAnimation(this, R.animator.practice_left_out);

        mProgressBar.setMax(100);

        mPresenter.onViewPrepared();
    }

    /**
     * Proceeds the quiz to it's next state.
     */
    public void proceed() {
        submitAnswer();
    }


    private void submitAnswer() {
        if (!showNextPage()) {
            showSummary();
        }
    }

    @Override
    public void updatePractices(Practice[] response) {
        mAdapter.setData(response);
        mPracticeView.setAdapter(mAdapter);
    }

    /**
     * Displays the next page.
     *
     * @return <code>true</code> if there's another quiz to solve, else <code>false</code>.
     */
    public boolean showNextPage() {
        if (null == mPracticeView) {
            return false;
        }
        int nextItem = mPracticeView.getDisplayedChild() + 1;
        setProgress(nextItem);
        final int count = mPracticeView.getAdapter().getCount();
        if (nextItem < count) {
            mPracticeView.showNext();
            return true;
        }
        return false;
    }

    private void animateProgressBar(int currentPracticePosition) {
        int progressBarPos = currentPracticePosition * 100 / mAdapter.getCount();

        mProgressBarAnimator.setIntValues(mProgressBar.getProgress(), progressBarPos);
        mProgressBarAnimator.start();
    }

    public void showSummary() {
        mProgressBar.setVisibility(View.GONE);

        int resId = getResources().getIdentifier("ic_medal", "drawable", this.getPackageName());
        mLessonEndIcon.setImageResource(resId);
        showSummaryAnimate(mLessonEndIcon);

        showSummaryAnimate(mLessonEndButton);

        mPracticeView.setVisibility(View.GONE);
    }

    private void showSummaryAnimate(View view) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(mInterpolator)
                .setStartDelay(300)
                .start();
    }
}
