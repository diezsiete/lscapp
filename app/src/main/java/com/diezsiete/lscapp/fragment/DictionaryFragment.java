package com.diezsiete.lscapp.fragment;


import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.DictionaryAdapter;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.DataManagerResponse;
import com.diezsiete.lscapp.data.db.model.Word;
import com.diezsiete.lscapp.widget.SignVideoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;



public class DictionaryFragment extends Fragment implements DictionaryAdapter.ListItemClickListener{

    private ViewGroup mContainer;
    private DictionaryAdapter mAdapter;
    private RecyclerView mDictionaryList;
    private LayoutInflater mInflater;

    private Toast mToast;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RelativeLayout mPopUpView;
    private View mPopUpCloseButton;
    private TextView mPopUpTextView;

    private SignVideoPlayer mPlayer;

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
        mInflater = inflater;
        // Inflate the layout for this fragment
        mContainer = (ViewGroup) inflater.inflate(R.layout.fragment_dictionary, container, false);

        mDictionaryList = mContainer.findViewById(R.id.rv_dictionary);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContainer.getContext());
        mDictionaryList.setLayoutManager(layoutManager);
        mDictionaryList.setHasFixedSize(true);

        mAdapter = new DictionaryAdapter(this);
        mDictionaryList.setAdapter(mAdapter);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = mContainer.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = mContainer.findViewById(R.id.pb_loading_indicator);

        mPopUpView = mContainer.findViewById(R.id.dictionary_popup);
        mPopUpTextView = mContainer.findViewById(R.id.tv_popup);
        mPopUpCloseButton = mContainer.findViewById(R.id.dictionary_clear_button);
        mPopUpCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(mContainer);
                mPopUpView.setVisibility(View.GONE);
                mPlayer.release();
            }
        });

        //exoplayer
        SimpleExoPlayerView exoPlayerView = mContainer.findViewById(R.id.dictionary_video_view);
        mPlayer = new SignVideoPlayer(getContext(), exoPlayerView);

        loadDictionaryData();

        return mContainer;
    }

    /**
     *
     */
    private void loadDictionaryData() {
        showDictionaryDataView();
        mLoadingIndicator.setVisibility(View.VISIBLE);


        DataManager.getWords(new DataManagerResponse<Word[]>(){
            @Override
            public void onResponse(Word[] response) {
                showDictionaryDataView();
                mAdapter.setDictionaryData(response);
            }
            @Override
            public void onFailure(Throwable t) {
                showErrorMessage();
                t.printStackTrace();
            }
        });
    }

    /**
     * Hace la View para el diccionario visible y esconde el mensaje de error
     */
    private void showDictionaryDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mDictionaryList.setVisibility(View.VISIBLE);
    }
    /**
     * Esconde la View del diccionary y muestra el mensaje de error
     */
    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mDictionaryList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onListItemClick(final Word concept) {
        if(mPopUpView.getVisibility() == View.GONE) {
            mPlayer.clearResources();
            mPlayer.addExternalResource(concept.getVideo());
            mPlayer.initialize();

            TransitionManager.beginDelayedTransition(mContainer);
            mPopUpTextView.setText(concept.getWord());
            mPopUpView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        mPlayer.release();
        super.onDetach();
    }
}
