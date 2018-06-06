package com.diezsiete.lscapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.databinding.DrawerHeaderBinding;
import com.diezsiete.lscapp.ui.binding.FragmentDataBindingComponent;
import com.diezsiete.lscapp.databinding.ActivityMainBinding;
import com.diezsiete.lscapp.viewmodel.DictionaryViewModel;
import com.diezsiete.lscapp.ui.view.signcamera.SignCameraManager;
import com.diezsiete.lscapp.util.AppConstants;
import com.diezsiete.lscapp.viewmodel.MainActivityViewModel;
import com.diezsiete.lscapp.viewmodel.UserViewModel;
import com.diezsiete.lscapp.vo.ToolbarData;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity
        implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    public NavigationController navigationController;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    DrawerLayout mDrawer;

    private MainActivityViewModel mainActivityViewModel;
    private DictionaryViewModel dictionaryViewModel;
    private UserViewModel userViewModel;

    private SignCameraManager signCameraManager;

    private MenuItem actionEdit;
    private MenuItem actionSearch;

    private boolean actionBack = false;

    private boolean onCreateNavigate = false;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main, dataBindingComponent);

        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);
        dictionaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(DictionaryViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawer = binding.drawerLayout;


        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mainActivityViewModel.goToLevel().observe(this, levelId -> {
            if(levelId != null && !levelId.isEmpty())
                navigationController.navigateToLevel(levelId);
        });
        dictionaryViewModel.getWords().observe(this, words -> {

        });

        mainActivityViewModel.getToolbarData().observe(this, this::handleToolbar);

        mainActivityViewModel.getDrawerData().observe(this, locked -> {
            if(locked != null)
                mDrawer.setDrawerLockMode(locked ?
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
        });

        mainActivityViewModel.getMenuItemSelected().observe(this, itemIndex ->{
            if(itemIndex != null)
                navigationView.getMenu().getItem(itemIndex).setChecked(true);
        });


        DrawerHeaderBinding drawerBind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.drawer_header, binding
                .navView, false);
        binding.navView.addHeaderView(drawerBind.getRoot());

        userViewModel.getUser().observe(this, user ->{
            if(!onCreateNavigate) {
                onCreateNavigate = true;
                if (user == null) {
                    navigationController.navigateToLogin();
                } else if (savedInstanceState == null) {
                    navigationController.navigateToLevelSelection();
                }
            }
            if(user != null)
                drawerBind.setUserEntity(user);
        });

        mainActivityViewModel.getToast().observe(this, toastData -> {
            if(toastData != null)
                Toast.makeText(MainActivity.this, toastData.message,
                        toastData.lengthShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);

        actionEdit = menu.findItem(R.id.action_edit);
        actionSearch = menu.findItem(R.id.action_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(actionBack){
                    navigationController.navigatePreviousFragment();
                }else
                    mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_edit:
                navigationController.navigateToProfileEdit();
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
        } else if (id == R.id.nav_profile) {
            navigationController.navigateToProfile();
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_sign_out) {
            onCreateNavigate = false;
            userViewModel.logout();
        }

        mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.CAMERA_REQUEST_PERMISSION: {
                if(signCameraManager != null) {
                    boolean permissionsGranted = true;
                    for (int grantResult : grantResults) {
                        if (grantResult == PackageManager.PERMISSION_DENIED)
                            permissionsGranted = false;
                    }
                    if(permissionsGranted)
                        signCameraManager.callOnPermissionGranted();
                    else
                        signCameraManager.callOnPermissionDenied(this);
                }
                break;
            }
        }
    }

    public SignCameraManager getSignCameraManager() {
        if(signCameraManager == null){
            signCameraManager = new SignCameraManager(this, getLifecycle());
        }
        return signCameraManager;
    }

    private void handleToolbar(ToolbarData toolbarData) {
        binding.setToolbarData(toolbarData);
        if(toolbarData != null) {
            if (toolbarData.show)
                getSupportActionBar().show();
            else
                getSupportActionBar().hide();

            if (!toolbarData.color.isEmpty())
                getWindow().setStatusBarColor(Color.parseColor(toolbarData.color));

            actionBack = toolbarData.actionBack;
            getSupportActionBar().setHomeAsUpIndicator(
                    toolbarData.actionBack ? R.drawable.ic_arrow_back : R.drawable.ic_menu);

            if (actionEdit != null)
                actionEdit.setVisible(toolbarData.actionEdit);
            if (actionSearch != null)
                actionSearch.setVisible(toolbarData.actionSearch);
        }
    }

}
