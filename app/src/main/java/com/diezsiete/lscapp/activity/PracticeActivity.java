package com.diezsiete.lscapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.fragment.PracticeFragment;
import com.diezsiete.lscapp.data.db.model.Level;

import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    private static final String TAG = "PracticeActivity";
    private static final String FRAGMENT_TAG = "Practice";

    private PracticeFragment mPracticeFragment;
    private String mLevelId;


    public static Intent getStartIntent(Context context, Level level) {
        Intent starter = new Intent(context, PracticeActivity.class);
        starter.putExtra(Level.TAG, level.getLevelId());
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        mLevelId = getIntent().getStringExtra(Level.TAG);

        populate(mLevelId);

        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    private void populate(String levelId) {
        if (null == levelId) {
            Log.w(TAG, "Didn't find a category. Finishing");
            finish();
        }


        setContentView(R.layout.activity_practice);

        startPractice();

        //initLayout(mCategory.getId());
        //initToolbar(mCategory);
    }

    private void startPractice() {
        initPracticeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.practice_fragment_container, mPracticeFragment, FRAGMENT_TAG)
                .commit();
        final FrameLayout container = (FrameLayout) findViewById(R.id.practice_fragment_container);
        container.setVisibility(View.VISIBLE);
    }

    private void initPracticeFragment() {
        if (mPracticeFragment != null) {
            return;
        }
        mPracticeFragment = PracticeFragment.newInstance(mLevelId);
    }

    /**
     * Proceeds the quiz to it's next state.
     */
    public void proceed() {
        submitAnswer();
    }


    private void submitAnswer() {
        if (!mPracticeFragment.showNextPage()) {
            mPracticeFragment.showSummary();
            //setResultSolved();
            return;
        }
    }

    //TODO
    private  static final int REQUEST_CAMERA_RESULT = 105;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Called when you request permission to read and write to external storage
        Log.d("JOSE", "onRequestPermissionsResult : " + requestCode);
        switch (requestCode) {
            case REQUEST_CAMERA_RESULT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
