package com.diezsiete.lscapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.DictionaryAdapter;
import com.diezsiete.lscapp.utils.NetworkUtils;
import com.diezsiete.lscapp.utils.OpenWeatherJsonUtils;

import java.net.URL;


public class DictionaryFragment extends Fragment implements DictionaryAdapter.ListItemClickListener{

    private DictionaryAdapter mAdapter;
    private RecyclerView mDictionaryList;

    private Toast mToast;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;


    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment newInstance() {
        DictionaryFragment fragment = new DictionaryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        mDictionaryList = view.findViewById(R.id.rv_dictionary);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mDictionaryList.setLayoutManager(layoutManager);
        mDictionaryList.setHasFixedSize(true);

        mAdapter = new DictionaryAdapter(this);
        mDictionaryList.setAdapter(mAdapter);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        loadDictionaryData();

        return view;
    }

    /**
     *
     */
    private void loadDictionaryData() {
        showDictionaryDataView();
        new FetchDictionaryTask(this.getContext()).execute("94043,USA");
    }

    /**
     * Hace la View para el diccionario visible y esconde el mensaje de error
     */
    private void showDictionaryDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mDictionaryList.setVisibility(View.VISIBLE);
    }
    /**
     * Esconde la View del diccionary y muestra el mensaje de error
     */
    private void showErrorMessage() {
        mDictionaryList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onListItemClick(String word) {
        Context context = this.getContext();
        Toast.makeText(context, word, Toast.LENGTH_SHORT)
                .show();
    }

    public class FetchDictionaryTask extends AsyncTask<String, Void, String[]> {

        private Context mContext;
        public FetchDictionaryTask(Context context){
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(mContext, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] dictionaryData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (dictionaryData != null) {
                showDictionaryDataView();
                mAdapter.setDictionaryData(dictionaryData);
            } else {
                showErrorMessage();
            }
        }
    }
}
