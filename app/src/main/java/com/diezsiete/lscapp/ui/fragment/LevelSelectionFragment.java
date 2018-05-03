package com.diezsiete.lscapp.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.di.component.ActivityComponent;
import com.diezsiete.lscapp.ui.activity.LevelActivity;
import com.diezsiete.lscapp.ui.adapter.LevelAdapter;
import com.diezsiete.lscapp.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class LevelSelectionFragment extends BaseFragment implements LevelSelectionContract.MvpView, LevelAdapter.Callback {

    @Inject
    LevelSelectionContract.Presenter<LevelSelectionContract.MvpView> mPresenter;

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
    public void openLevelActivity() {
        startActivity(LevelActivity.getStartIntent(getBaseActivity()));
        //getBaseActivity().finish();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
