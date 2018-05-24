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

package com.diezsiete.lscapp.util;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterViewFlipper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * based on https://gist.github.com/baconpat/8405a88d04bd1942eb5e430d33e4faa2
 * license MIT
 */
public class AdapterViewFlipperMatcher {

    private final int viewId;

    public AdapterViewFlipperMatcher(int viewId) {
        this.viewId = viewId;
    }

    public Matcher<View> atPosition(final int position) {
        return atPositionOnView(position, -1);
    }

    public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            public void describeTo(Description description) {
                String idDescription = Integer.toString(viewId);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(viewId);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format("%s (resource name not found)", viewId);
                    }
                }

                description.appendText("RecyclerView with id: " + idDescription + " at position: " + position);
            }

            public boolean matchesSafely(View view) {

                this.resources = view.getResources();

                if (childView == null) {
                    AdapterViewFlipper adapterViewFlipper = view.getRootView().findViewById(viewId);
                    if (adapterViewFlipper != null && adapterViewFlipper.getId() == viewId) {
                        childView = adapterViewFlipper.getSelectedView();
                    }
                    else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }
            }
        };
    }
}