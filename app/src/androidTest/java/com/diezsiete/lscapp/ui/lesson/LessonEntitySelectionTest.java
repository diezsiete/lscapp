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

package com.diezsiete.lscapp.ui.lesson;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.binding.FragmentBindingAdapters;
import com.diezsiete.lscapp.testing.SingleFragmentActivity;
import com.diezsiete.lscapp.viewmodel.MainActivityViewModel;
import com.diezsiete.lscapp.ui.NavigationController;
import com.diezsiete.lscapp.viewmodel.LessonViewModel;
import com.diezsiete.lscapp.ui.fragment.LevelFragment;
import com.diezsiete.lscapp.viewmodel.LevelViewModel;
import com.diezsiete.lscapp.util.EspressoTestUtil;
import com.diezsiete.lscapp.util.RecyclerViewMatcher;
import com.diezsiete.lscapp.util.TaskExecutorWithIdlingResourceRule;
import com.diezsiete.lscapp.util.TestUtil;
import com.diezsiete.lscapp.util.ViewModelUtil;
import com.diezsiete.lscapp.db.entity.LessonEntity;
import com.diezsiete.lscapp.db.entity.LevelEntity;
import com.diezsiete.lscapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LessonEntitySelectionTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private MutableLiveData<Resource<LevelEntity>> level = new MutableLiveData<>();
    private MutableLiveData<Resource<List<LessonEntity>>> lessons = new MutableLiveData<>();

    private LevelFragment levelFragment;

    private LevelViewModel levelViewModel;
    private LessonViewModel lessonViewModel;
    private MainActivityViewModel mainActivityViewModel;

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;


    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        levelFragment = LevelFragment.create("sustantivo");

        levelViewModel = mock(LevelViewModel.class);
        lessonViewModel = mock(LessonViewModel.class);
        mainActivityViewModel = mock(MainActivityViewModel.class);

        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);

        doNothing().when(levelViewModel).setId(anyString());
        doNothing().when(lessonViewModel).setId(anyString());
        when(levelViewModel.getLevel()).thenReturn(level);
        when(lessonViewModel.getResults()).thenReturn(lessons);

        levelFragment.levelViewModelFactory = ViewModelUtil.createFor(levelViewModel);
        levelFragment.lessonViewModelFactory = ViewModelUtil.createFor(lessonViewModel);
        levelFragment.mainActivityViewModelFactory = ViewModelUtil.createFor(mainActivityViewModel);

        levelFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        levelFragment.navigationController = navigationController;
        activityRule.getActivity().setFragment(levelFragment);
    }


    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.lessons);
    }

    /**
     * Prueba que mientras se cargan las lección se muestra icono "cargando"
     */
    @Test
    public void loadingLessonsFeedback() {
        lessons.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }


    /**
     * Prueba que las lecciones se cargarón exitosamente
     */
    @Test
    public void loadedLessonsSuccess() {
        setLessons("en el estudio", "actividades cotidianas");
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("en el estudio"))));
        onView(listMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("actividades cotidianas"))));
    }

    /**
     * Prueba el caso de que las lecciones no se hallan cargado correctamente, se muestra
     * correctamente el boton de reintentar y este funciona correctamentte
     * @throws InterruptedException
     */
    @Test
    public void loadedLessonsErrorAndTryAgain() throws InterruptedException {
        lessons.postValue(Resource.error("foo", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(lessonViewModel).retry();
        lessons.postValue(Resource.loading(null));

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));

        setLessons("en el estudio", "actividades cotidianas");

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("en el estudio"))));
        onView(listMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("actividades cotidianas"))));
    }

    @Test
    public void clickLessonNavigateToPractices() {
        setLessons("en el estudio");
        onView(withText("Practicar")).perform(click());
        verify(navigationController).navigateToLesson("en el estudio");
    }

    private void setLessons(String... names) {
        //LevelEntity level = TestUtil.createLevel("sustantivos", "Sustantivos");
        List<LessonEntity> lessonEntities = new ArrayList<>();
        for (String name : names) {
            lessonEntities.add(TestUtil.createLesson(name, name));
        }
        this.lessons.postValue(Resource.success(lessonEntities));
    }
}