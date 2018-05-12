package com.diezsiete.lscapp.ui.practice;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscoverImageAdapter extends BaseAdapter {
    private Fragment fragment;

    public DiscoverImageAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Integer getItem(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        return i == 0 ? new DiscoverImageVideoView(fragment) : new DiscoverImageImagesView(fragment);
    }

}