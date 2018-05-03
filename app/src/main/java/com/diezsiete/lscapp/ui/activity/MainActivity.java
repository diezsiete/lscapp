package com.diezsiete.lscapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.fragment.LevelSelectionFragment;
import com.diezsiete.lscapp.ui.base.BaseActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity
        implements MainContract.MvpView, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainContract.Presenter<MainContract.MvpView> mPresenter;

    DrawerLayout mDrawer;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void setUp() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //menu item practicar seleccionado por default
        //navigationView.getMenu().getItem(0).setChecked(true);
        //attachFragment(LevelSelectionFragment.newInstance());
        mPresenter.onViewInitialized();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    public void onBackPressed() {
        closeNavigationDrawer();
        super.onBackPressed();
    }

    @Override
    public void showLevelSelectionFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, LevelSelectionFragment.newInstance())
                .commit();
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_practice) {
            //attachFragment(LevelSelectionFragment.newInstance());
        } else if (id == R.id.nav_dictionary) {
            //attachFragment(DictionaryFragment.newInstance());
        } else if (id == R.id.nav_leaderboard) {

        } else if (id == R.id.nav_configuration) {
            //attachFragment(SettingsFragment.newInstance());

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_sign_out) {
            mPresenter.onDrawerOptionLogoutClick();
        }

        return true;
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void closeNavigationDrawer() {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
    }
}
