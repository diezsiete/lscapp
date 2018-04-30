package com.diezsiete.lscapp.ui.level;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.activity.PracticeActivity;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.di.component.ActivityComponent;
import com.diezsiete.lscapp.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class LevelSelectionFragment extends BaseFragment implements LevelContract.MvpView, LevelAdapter.Callback {

    @Inject
    LevelContract.Presenter<LevelContract.MvpView> mPresenter;

    @Inject
    LevelAdapter mAdapter;

    @BindView(R.id.levels) RecyclerView mRecyclerView;


    public static LevelSelectionFragment newInstance() {
        return new LevelSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_selection, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            mAdapter.setCallback(this);
        }

        return view;
    }

    @Override
    protected void setUp(View view) {
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.onViewPrepared();
    }


    @Override
    public void updateLevels(Level[] levels){
        mAdapter.setData(levels);
    }

    @Override
    public void onLevelClick(Level level) {
        mPresenter.onLevelClicked(level);
    }

    @Override
    public void openPracticeActivity(Level level) {
        startActivity(PracticeActivity.getStartIntent(getBaseActivity(), level));
        //getBaseActivity().finish();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
