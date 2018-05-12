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

public class PracticeAdapter extends BaseAdapter {

    private List<String> items;
    private Context context;
    private Fragment fragment;
    private List<String> types;

    public PracticeAdapter(Context context, Fragment f) {
        this.context = context;
        this.fragment = f;
    }

    @Override
    public int getCount() {
        if(items == null)
            return 0;
        return items.size();
    }

    @Override
    public int getViewTypeCount() {
        if(items == null)
            return 0;
        Set<String> tmpTypes = new HashSet<>();
        for (int i = 0; i < items.size(); i++) {
            tmpTypes.add(items.get(i));
        }
        types = new ArrayList<>(tmpTypes);
        return types.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return types.indexOf(getItem(position));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final String practice = getItem(i);
        //TODO
        if (convertView instanceof ShowSignView) {
            return convertView;
            /*if (((AbsPracticeView) convertView).getPractice().equals(practice)) {
                return convertView;
            }*/
        }

        convertView = createViewFor(practice);
        return convertView;
    }

    public void setData(List<String> data) {
        items = data;
        notifyDataSetChanged();
    }


    private View createViewFor(String code) {
        switch (code) {
            case "show-sign":
                return new ShowSignView(fragment);
            case "which-one-videos":
                return new WhichOneVideosView(fragment);
            case "which-one-video":
                return new WhichOneVideoView(fragment);
            case "translate-video":
                return new TranslateVideoView(fragment);
            case "discover-image":
                return new DiscoverImageView(fragment);
            case "take-sign":
                return new TakeSignView(fragment);

        }
        throw new UnsupportedOperationException(
                "Quiz of type " + code + " can not be displayed.");
    }



}