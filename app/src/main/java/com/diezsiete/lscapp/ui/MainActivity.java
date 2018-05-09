package com.diezsiete.lscapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.common.NavigationController;
import com.diezsiete.lscapp.ui.dictionary.DictionaryViewModel;
import com.diezsiete.lscapp.ui.level.LevelViewModel;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity
        implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    NavigationController navigationController;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    DrawerLayout mDrawer;

    private MainActivityViewModel mainActivityViewModel;
    private DictionaryViewModel dictionaryViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);
        dictionaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(DictionaryViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        mDrawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //menu item practicar seleccionado por default
        navigationView.getMenu().getItem(0).setChecked(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivityViewModel.getShowBackButton().observe(this, showBackButton -> {
            if(showBackButton != null) {
                getSupportActionBar().setHomeAsUpIndicator(
                    showBackButton ? R.drawable.ic_arrow_back : R.drawable.ic_menu);
            }
        });

        if (savedInstanceState == null) {
            navigationController.navigateToLevelSelection();
        }

        mainActivityViewModel.goToLevel().observe(this, levelId -> {
            if(levelId != null && !levelId.isEmpty())
                navigationController.navigateToLevel(levelId);
        });
        dictionaryViewModel.getWords().observe(this, words -> {

        });
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Boolean showBackButton = mainActivityViewModel.getShowBackButton().getValue();
                if(showBackButton != null && showBackButton){
                    navigationController.navigateToLevelSelection();
                }else
                    mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_practice) {
            navigationController.navigateToLevelSelection();
        } else if (id == R.id.nav_dictionary) {
            navigationController.navigateToDictionary();
        } else if (id == R.id.nav_leaderboard) {

        } else if (id == R.id.nav_configuration) {
            //attachFragment(SettingsFragment.newInstance());

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_sign_out) {
            //mPresenter.onDrawerOptionLogoutClick();
        }

        mDrawer.closeDrawers();
        return true;
    }
}
