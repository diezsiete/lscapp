package com.diezsiete.lscapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.activity.PracticeActivity;
import com.diezsiete.lscapp.adapter.LevelAdapter;
import com.diezsiete.lscapp.model.Level;
import com.diezsiete.lscapp.rest.ProxyApp;

/**
 *
 */
public class LevelSelectionFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LevelAdapter mAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private FloatingActionButton mErrorReloadButton;

    public LevelSelectionFragment() {
        // Required empty public constructor
    }


    public static LevelSelectionFragment newInstance() {
        LevelSelectionFragment fragment = new LevelSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_selection, container, false);

        mErrorMessageDisplay = view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        mErrorReloadButton = view.findViewById(R.id.error_reload_button);

        mErrorReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLevelsData();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lessons);
        setUpLessonGrid(mRecyclerView);
        loadLevelsData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpLessonGrid(final RecyclerView levelsView) {
        //final int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_nano);
        //categoriesView.addItemDecoration(new OffsetDecoration(spacing));
        mAdapter = new LevelAdapter(getActivity());
        mAdapter.setOnItemClickListener(
                new LevelAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Level level) {
                        Activity activity = getActivity();
                        startPracticeActivity(activity, level);
                    }
                });
        levelsView.setAdapter(mAdapter);
    }

    /**
     * Hace la View de informacion visible y esconde el mensaje de error
     */
    private void showDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mErrorReloadButton.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    /**
     * Esconde la View de informacion y muestra el mensaje de error
     */
    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mErrorReloadButton.setVisibility(View.VISIBLE);
    }

    private void startPracticeActivity(Activity activity, Level level) {
        Intent startIntent = PracticeActivity.getStartIntent(activity, level);
        startActivity(startIntent);
    }


    private void loadLevelsData() {
        showDataView();
        mLoadingIndicator.setVisibility(View.VISIBLE);

        ProxyApp.getLevels(new ProxyApp.LSCResponse<Level[]>(){
            @Override
            public void onResponse(Level[] response) {
                showDataView();
                mAdapter.setData(response);
            }
            @Override
            public void onFailure() {
                showErrorMessage();
            }
        });
    }
}
