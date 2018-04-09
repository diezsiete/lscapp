package com.diezsiete.lscapp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.PracticeAdapter;
import com.diezsiete.lscapp.model.Level;
import com.diezsiete.lscapp.model.practice.Practice;
import com.diezsiete.lscapp.utils.ProxyApp;

import java.util.List;


public class PracticeFragment extends Fragment {

    private AdapterViewAnimator mPracticeView;
    private PracticeAdapter mAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String mLevelId;


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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        mErrorMessageDisplay = view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPracticeView = (AdapterViewAnimator) view.findViewById(R.id.practice_view);
        mPracticeView.setAdapter(getPracticeAdapter());
        //int position = mCategory.getFirstUnsolvedQuizPosition();
        //mPracticeView.setSelection(0);
        new FetchTask().execute();
        super.onViewCreated(view, savedInstanceState);
    }

    private PracticeAdapter getPracticeAdapter() {
        if (null == mAdapter) {
            mAdapter = new PracticeAdapter(getActivity(), mLevelId);
        }
        return mAdapter;
    }


    public class FetchTask extends AsyncTask<String, Void, List<Practice>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected List<Practice> doInBackground(String... params) {
            try {
                return ProxyApp.getPractices(mLevelId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<Practice> data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null) {
                showDataView();
                getPracticeAdapter().setData(data);
                //mPracticeView.setSelection(0);
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * Hace la View de informacion visible y esconde el mensaje de error
     */
    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mPracticeView.setVisibility(View.VISIBLE);
    }
    /**
     * Esconde la View de informacion y muestra el mensaje de error
     */
    private void showErrorMessage() {
        mPracticeView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
