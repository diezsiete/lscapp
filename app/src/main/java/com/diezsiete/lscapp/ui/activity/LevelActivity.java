package com.diezsiete.lscapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Lesson;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.ui.adapter.LessonAdapter;
import com.diezsiete.lscapp.ui.base.BaseActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LevelActivity extends BaseActivity implements LevelContract.MvpView, LessonAdapter.Callback {

    @Inject
    LevelContract.Presenter<LevelContract.MvpView> mPresenter;
    @Inject
    LessonAdapter mAdapter;

    private static final String TAG = "LevelActivity";
    private static final String FRAGMENT_TAG = "Lesson";

    @BindView(R.id.level_title) TextView mLevelTitle;
    @BindView(R.id.level_image) ImageView mLevelImage;
    @BindView(R.id.lessons) RecyclerView mRecyclerView;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LevelActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        mAdapter.setCallback(this);

        setUp();
    }

    @Override
    protected void setUp() {
        mRecyclerView.setAdapter(mAdapter);
        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mPresenter.onViewPrepared();
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

    @Override
    public void setLevel(Level level) {
        mLevelTitle.setText(level.getTitle());
        Picasso.with(this).load(level.getImage())
                .resize(200, 200)
                .into(mLevelImage);
    }


    @Override
    public void updateLessons(Lesson[] lessons) {
        mAdapter.setData(lessons);
    }

    @Override
    public void openLessonActivity() {
        startActivity(LessonActivity.getStartIntent(this));
        //finish();
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        mPresenter.onLessonClicked(lesson);
    }
}
