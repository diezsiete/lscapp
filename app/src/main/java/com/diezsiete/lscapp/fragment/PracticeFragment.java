package com.diezsiete.lscapp.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterViewAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.PracticeAdapter;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.DataManagerResponse;

import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.data.db.model.Practice;


public class PracticeFragment extends Fragment {

    private AdapterViewAnimator mPracticeView;
    private PracticeAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String mLevelId;

    private Interpolator mInterpolator;

    private ObjectAnimator mProgressBarAnimator;

    public static PracticeFragment newInstance(String levelId) {
        Bundle args = new Bundle();
        args.putString(Level.TAG, levelId);
        PracticeFragment fragment = new PracticeFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLevelId = getArguments().getString(Level.TAG);
        mInterpolator = new AccelerateInterpolator();


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        mErrorMessageDisplay = view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        mProgressBar = view.findViewById(R.id.progress);

        mProgressBarAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 0);
        mProgressBarAnimator.setDuration(600);
        mProgressBarAnimator.setInterpolator(mInterpolator);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPracticeView = (AdapterViewAnimator) view.findViewById(R.id.practice_view);

        mPracticeView.setInAnimation(getActivity(), R.animator.practice_right_in);
        mPracticeView.setOutAnimation(getActivity(), R.animator.practice_left_out);

        getPracticeData();

        super.onViewCreated(view, savedInstanceState);
    }

    private PracticeAdapter getPracticeAdapter() {
        if (null == mAdapter) {
            mAdapter = new PracticeAdapter(getActivity(), mLevelId);
        }
        return mAdapter;
    }

    private void getPracticeData() {
        mPracticeView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        /*DataManager.getPracticesByLevel(mLevelId, new DataManagerResponse<Practice[]>() {
            @Override
            public void onResponse(Practice[] response) {
                getPracticeAdapter().setData(response);
                mPracticeView.setAdapter(getPracticeAdapter());
                mProgressBar.setMax(100);
                showDataView();
            }
            @Override
            public void onFailure(Throwable t) {
                showErrorMessage();
            }
        });*/
    }

    /**
     * Hace la View de informacion visible y esconde el mensaje de error
     */
    private void showDataView() {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.GONE);
        mPracticeView.setVisibility(View.VISIBLE);
    }
    /**
     * Esconde la View de informacion y muestra el mensaje de error
     */
    private void showErrorMessage() {
        mPracticeView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
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

    private void setProgress(int currentPracticePosition) {
        if (!isAdded()) {
            return;
        }
        int progressBarPos = currentPracticePosition * 100 / getPracticeAdapter().getCount();

        mProgressBarAnimator.setIntValues(mProgressBar.getProgress(), progressBarPos);
        mProgressBarAnimator.start();
    }

    public void showSummary() {
        mProgressBar.setVisibility(View.GONE);

        ImageView mIcon = getView().findViewById(R.id.success_icon);
        int resId = getResources().getIdentifier("ic_medal", "drawable", getContext().getPackageName());
        mIcon.setImageResource(resId);
        showSummaryAnimate(mIcon);

        Button buttonEnd = getView().findViewById(R.id.practice_end_button);
        showSummaryAnimate(buttonEnd);

        final Activity practiceActivity = getActivity();
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavUtils.navigateUpFromSameTask(practiceActivity);
            }
        });

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
