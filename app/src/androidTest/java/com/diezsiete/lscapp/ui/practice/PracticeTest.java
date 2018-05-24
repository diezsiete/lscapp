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

package com.diezsiete.lscapp.ui.practice;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.AdapterViewFlipper;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.binding.FragmentBindingAdapters;
import com.diezsiete.lscapp.testing.SingleFragmentActivity;
import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.common.NavigationController;
import com.diezsiete.lscapp.ui.lesson.LessonFragment;
import com.diezsiete.lscapp.ui.level.LessonViewModel;
import com.diezsiete.lscapp.ui.level.LevelFragment;
import com.diezsiete.lscapp.ui.level.LevelViewModel;
import com.diezsiete.lscapp.util.AdapterViewFlipperMatcher;
import com.diezsiete.lscapp.util.EspressoTestUtil;
import com.diezsiete.lscapp.util.RecyclerViewMatcher;
import com.diezsiete.lscapp.util.TaskExecutorWithIdlingResourceRule;
import com.diezsiete.lscapp.util.TestUtil;
import com.diezsiete.lscapp.util.ViewModelUtil;
import com.diezsiete.lscapp.vo.AnswerMessage;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class PracticeTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();


    private MutableLiveData<List<String>> practicesCodes = new MutableLiveData<>();
    private MutableLiveData<PracticeWithData> currentPractice = new MutableLiveData<>();
    private MutableLiveData<Lesson> lessonNoProgress = new MutableLiveData<>();
    private MutableLiveData<AnswerMessage> answerMessage = new MutableLiveData<>();
    private MutableLiveData<String> goToLevel = new MutableLiveData<>();
    private MutableLiveData<Boolean> showNext = new MutableLiveData<>();

    private LessonFragment lessonFragment;

    private PracticeViewModel practiceViewModel;
    private LessonViewModel lessonViewModel;
    private MainActivityViewModel mainActivityViewModel;

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;


    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        lessonFragment = new LessonFragment();

        practiceViewModel = mock(PracticeViewModel.class);
        lessonViewModel = mock(LessonViewModel.class);
        mainActivityViewModel = mock(MainActivityViewModel.class);

        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);

        doNothing().when(practiceViewModel).setId(anyString());
        doNothing().when(lessonViewModel).setId(anyString());
        when(practiceViewModel.getPracticesCodes()).thenReturn(practicesCodes);
        when(practiceViewModel.getCurrentPractice()).thenReturn(currentPractice);
        when(lessonViewModel.getLessonNoProgress()).thenReturn(lessonNoProgress);
        when(practiceViewModel.answerMessage()).thenReturn(answerMessage);
        when(practiceViewModel.goToLevel()).thenReturn(goToLevel);
        when(practiceViewModel.showNext()).thenReturn(showNext);

        lessonFragment.lessonViewModelFactory = ViewModelUtil.createFor(lessonViewModel);
        lessonFragment.practiceViewModelFactory = ViewModelUtil.createFor(practiceViewModel);
        lessonFragment.mainActivityViewModelFactory = ViewModelUtil.createFor(mainActivityViewModel);

        lessonFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        activityRule.getActivity().setFragment(lessonFragment);
    }


    @NonNull
    private AdapterViewFlipperMatcher listMatcher() {
        return new AdapterViewFlipperMatcher(R.id.practice_view);
    }

    @NonNull
    private RecyclerViewMatcher optionListMatcher() {
        return new RecyclerViewMatcher(R.id.grid_view);
    }


    /**
     * Prueba que las lecciones se cargarón exitosamente
     */
    @Test
    public void loadedPracticeSuccess() {
        Lesson lesson = TestUtil.createLesson("en el estudio", "en el estudio");
        this.lessonNoProgress.postValue(lesson);
        setPracticesCodes("show-sign");
        this.currentPractice.postValue(TestUtil.createShowSign(lesson,"A"));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("A"))));
    }

    /**
     * Valida que la practica al iniciar el boton de inicar esta desactivado
     */
    @Test
    public void practiceNoAnswerNoContinue(){
        Lesson lesson = TestUtil.createLesson("en el estudio", "en el estudio");
        this.lessonNoProgress.postValue(lesson);
        setPracticesCodes("which-one-video");
        this.currentPractice.postValue(TestUtil.createWhichOneVideo(lesson, "A", "B", "C"));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withId(R.id.submitAnswer))));
        onView(withId(R.id.submitAnswer)).check(matches(not(isEnabled())));
        //onView(listMatcher().atPosition(0)).check(matches(not(isEnabled())));
    }
    @Test
    public void optionSelectedContinueEnabled(){
        Lesson lesson = TestUtil.createLesson("en el estudio", "en el estudio");
        this.lessonNoProgress.postValue(lesson);
        setPracticesCodes("which-one-video");

        PracticeWithData practice = TestUtil.createWhichOneVideo(lesson, "A", "B", "C");
        practice.entity.answerUser.add(0);
        practice.entity.answer.add(0);
        this.currentPractice.postValue(practice);
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withId(R.id.submitAnswer))));
        //onView(optionListMatcher().atPosition(0)).perform(click());
        onView(withId(R.id.submitAnswer)).check(matches(isEnabled()));
    }

    /**
     * Prueba que valida en respuesta correcta se de la retroalimentación esperada
     */
    @Test
    public void practiceAnswerCorrect(){
        Lesson lesson = TestUtil.createLesson("en el estudio", "en el estudio");
        this.lessonNoProgress.postValue(lesson);
        setPracticesCodes("which-one-video");
        PracticeWithData practice = TestUtil.createWhichOneVideo(lesson, "A", "B", "C");
        practice.entity.answerUser.add(0);
        practice.entity.answer.add(0);
        this.currentPractice.postValue(practice);
        answerMessage.postValue(AnswerMessage.success(""));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withId(R.id.submitAnswer))));
        onView(withId(R.id.answer_message_cont)).check(matches(hasDescendant(withText(getString(R.string.practice_success_message)))));
    }

    /**
     * Prueba que valida en respuesta correcta se de la retroalimentación esperada
     */
    @Test
    public void practiceAnswerIncorrect(){
        Lesson lesson = TestUtil.createLesson("en el estudio", "en el estudio");
        this.lessonNoProgress.postValue(lesson);
        setPracticesCodes("which-one-video");
        PracticeWithData practice = TestUtil.createWhichOneVideo(lesson, "A", "B", "C");
        practice.entity.answerUser.add(0);
        practice.entity.answer.add(0);
        this.currentPractice.postValue(practice);
        answerMessage.postValue(AnswerMessage.danger(""));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withId(R.id.submitAnswer))));
        onView(withId(R.id.answer_message_cont)).check(matches(hasDescendant(withText(getString(R.string.practice_warning_message)))));
    }


    private void setPracticesCodes(String... names) {
        List<String> practicesCodes = new ArrayList<>();
        for (String code : names) {
            practicesCodes.add(code);
        }
        this.practicesCodes.postValue(practicesCodes);
    }

    private String getString(@StringRes int id, Object... args) {
        return InstrumentationRegistry.getTargetContext().getString(id, args);
    }
}