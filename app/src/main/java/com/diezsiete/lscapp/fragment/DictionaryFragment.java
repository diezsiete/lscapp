package com.diezsiete.lscapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.DictionaryAdapter;
import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.utils.DictioinaryJsonUtils;
import com.diezsiete.lscapp.utils.NetworkUtils;
import com.diezsiete.lscapp.utils.ProxyApp;
import com.diezsiete.lscapp.widget.SignVideoPlayer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;


public class DictionaryFragment extends Fragment implements DictionaryAdapter.ListItemClickListener{

    private DictionaryAdapter mAdapter;
    private RecyclerView mDictionaryList;
    private LayoutInflater mInflater;

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
        mInflater = inflater;
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
        new FetchDictionaryTask(this.getContext()).execute();
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
    public void onListItemClick(final Concept concept) {

        View popupView = mInflater.inflate(R.layout.dictionary_popup_window, null);

        TextView textView = (TextView) popupView.findViewById(R.id.tv_dictionary_popup_window);
        textView.setText(concept.getMeaning());


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);


        //exoplayer
        //SimpleExoPlayerView exoPlayerView = popupView.findViewById(R.id.dictionary_video_view);
        //SignVideoPlayer player = new SignVideoPlayer(popupView.getContext(), exoPlayerView);
        //player.addExternalResource("https://lscapp.pta.com.co/video/APARTAMENTO.mp4");
        //player.initialize();

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public class FetchDictionaryTask extends AsyncTask<String, Void, Concept[]> {

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
        protected Concept[] doInBackground(String... params) {
            try {
                return ProxyApp.getDictionary();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Concept[] dictionaryData) {
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
