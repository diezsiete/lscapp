package com.diezsiete.lscapp.ui.practice;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * A simple adapter to display a options of a quiz.
 */
public class PracticeOptionsTextAdapter extends BaseAdapter {

    private final List<String> mOptions;
    private final int mLayoutId;


    /**
     * Creates an {@link PracticeOptionsTextAdapter}.
     *
     * @param options The options to add to the adapter.
     * @param layoutId Must consist of a single {@link TextView}.
     */
    public PracticeOptionsTextAdapter(List<String> options, @LayoutRes int layoutId) {
        mOptions = options;
        mLayoutId = layoutId;
    }


    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public String getItem(int position) {
        return mOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        /* Important to return tru ein order to get checked items from this adapter correctly */
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(mLayoutId, parent, false);
        }
        String text = getText(position);
        ((TextView) convertView).setText(text);
        return convertView;
    }

    private String getText(int position) {
        String text;
        return getItem(position);
    }
}