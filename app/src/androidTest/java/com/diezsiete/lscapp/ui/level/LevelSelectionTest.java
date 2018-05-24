/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diezsiete.lscapp.ui.level;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentBindingAdapters;
import com.diezsiete.lscapp.testing.SingleFragmentActivity;
import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.common.NavigationController;
import com.diezsiete.lscapp.ui.level.LevelSelectionFragment;
import com.diezsiete.lscapp.ui.level.LevelViewModel;
import com.diezsiete.lscapp.util.EspressoTestUtil;
import com.diezsiete.lscapp.util.RecyclerViewMatcher;
import com.diezsiete.lscapp.util.TaskExecutorWithIdlingResourceRule;
import com.diezsiete.lscapp.util.TestUtil;
import com.diezsiete.lscapp.util.ViewModelUtil;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LevelSelectionTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;

    private LevelViewModel levelViewModel;
    private MainActivityViewModel mainActivityViewModel;

    private MutableLiveData<Resource<List<Level>>> results = new MutableLiveData<>();
    //private MutableLiveData<SearchViewModel.LoadMoreState> loadMoreStatus = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        LevelSelectionFragment levelSelectionFragment = new LevelSelectionFragment();
        levelViewModel = mock(LevelViewModel.class);
        mainActivityViewModel = mock(MainActivityViewModel.class);
        //doReturn(loadMoreStatus).when(viewModel).getLoadMoreStatus();
        when(levelViewModel.getResults()).thenReturn(results);

        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);

        levelSelectionFragment.levelViewModelFactory = ViewModelUtil.createFor(levelViewModel);
        levelSelectionFragment.mainActivityViewModelFactory = ViewModelUtil.createFor(mainActivityViewModel);
        levelSelectionFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        levelSelectionFragment.navigationController = navigationController;

        activityRule.getActivity().setFragment(levelSelectionFragment);
    }


    /**
     * Prueba que mientras se cargan los niveles se muestra icono "cargando"
     */
    @Test
    public void loadingLevelsFeedback() {
        results.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    /**
     * Prueba se muestran niveles cargados correctamente
     */
    @Test
    public void loadedLevelsSuccess() {
        Level level = TestUtil.createLevel("sustantivos", "Sustantivos");
        results.postValue(Resource.success(Arrays.asList(level)));
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("Sustantivos"))));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    /**
     * Prueba que si hay error al cargar niveles se muestra opción de reintentar
     */
    @Test
    public void loadedLevelsError() {
        results.postValue(Resource.error("failed to load", null));
        onView(withId(R.id.error_msg)).check(matches(isDisplayed()));
    }

    /**
     * Prueba que reintentar funciona correctamente
     */
    @Test
    public void onRetryLoadedLevelsSuccess() {
        results.postValue(Resource.error("foo", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(levelViewModel).retry();
        results.postValue(Resource.loading(null));

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));

        Level level = TestUtil.createLevel("sustantivos", "Sustantivos");
        results.postValue(Resource.success(Arrays.asList(level)));
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("Sustantivos"))));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void clickLevelNavigateToLessonSelection() throws Throwable {
        Level level = TestUtil.createLevel("sustantivos", "Sustantivos");
        results.postValue(Resource.success(Arrays.asList(level)));
        onView(withText("Practicar")).perform(click());
        verify(navigationController).navigateToLevel("sustantivos");
    }


    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.levels);
    }
}