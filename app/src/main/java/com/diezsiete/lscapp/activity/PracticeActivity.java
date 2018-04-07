package com.diezsiete.lscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.model.Level;

public class PracticeActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context, Level level) {
        Intent starter = new Intent(context, PracticeActivity.class);
        starter.putExtra(Level.TAG, level.getId());
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
    }
}
