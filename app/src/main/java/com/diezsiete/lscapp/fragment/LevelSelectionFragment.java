package com.diezsiete.lscapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.activity.PracticeActivity;
import com.diezsiete.lscapp.adapter.LevelAdapter;
import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.model.Level;
import com.diezsiete.lscapp.utils.ProxyApp;

/**
 *
 */
public class LevelSelectionFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LevelAdapter mAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

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
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lessons);
        setUpLessonGrid(mRecyclerView);
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadData() {
        showDataView();
        new FetchTask().execute();
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
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    /**
     * Esconde la View de informacion y muestra el mensaje de error
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void startPracticeActivity(Activity activity, Level level) {
        Intent startIntent = PracticeActivity.getStartIntent(activity, level);
        startActivity(startIntent);
    }



    public class FetchTask extends AsyncTask<String, Void, Level[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected Level[] doInBackground(String... params) {
            try {
                return ProxyApp.getLevels();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Level[] data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null) {
                showDataView();
                mAdapter.setData(data);
            } else {
                showErrorMessage();
            }
        }
    }
}
