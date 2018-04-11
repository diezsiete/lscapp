package com.diezsiete.lscapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.diezsiete.lscapp.model.practice.DiscoverImagePractice;
import com.diezsiete.lscapp.model.practice.Practice;
import com.diezsiete.lscapp.model.practice.ShowSignPractice;
import com.diezsiete.lscapp.model.practice.TakePicturePractice;
import com.diezsiete.lscapp.model.practice.TranslateVideoPractice;
import com.diezsiete.lscapp.model.practice.WhichOneVideoPractice;
import com.diezsiete.lscapp.model.practice.WhichOneVideosPractice;
import com.diezsiete.lscapp.widget.practice.AbsPracticeView;
import com.diezsiete.lscapp.widget.practice.DiscoverImagePracticeView;
import com.diezsiete.lscapp.widget.practice.ShowSignPracticeView;
import com.diezsiete.lscapp.widget.practice.TakePicturePracticeView;
import com.diezsiete.lscapp.widget.practice.TranslateVideoPracticeView;
import com.diezsiete.lscapp.widget.practice.WhichOneVideoPracticeView;
import com.diezsiete.lscapp.widget.practice.WhichOneVideosPracticeView;

import java.util.List;


public class PracticeAdapter extends BaseAdapter{

    private final Context mContext;
    private final String mLevelId;
    private List<Practice> mData;

    public PracticeAdapter(Context context, String levelId) {
        mContext = context;
        mLevelId = levelId;
    }

    @Override
    public int getCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public int getViewTypeCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Practice getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final Practice practice = getItem(i);
        //TODO
        /*if (convertView instanceof AbsPracticeView) {
            if (((AbsPracticeView) convertView).getPractice().equals(practice)) {
                return convertView;
            }
        }*/

        convertView = getViewInternal(practice);
        return convertView;
    }

    public void setData(List<Practice> data) {
        mData = data;
        notifyDataSetChanged();
    }


    private AbsPracticeView getViewInternal(Practice practice) {
        if (null == practice) {
            throw new IllegalArgumentException("Practice must not be null");
        }
        return createViewFor(practice);
    }

    private AbsPracticeView createViewFor(Practice practice) {
        switch (practice.getType()) {
            case SHOW_SIGN:
                return new ShowSignPracticeView(mContext, (ShowSignPractice) practice);
            case WHICH_ONE_VIDEOS:
                return new WhichOneVideosPracticeView(mContext, (WhichOneVideosPractice) practice);
            case WHICH_ONE_VIDEO:
                return new WhichOneVideoPracticeView(mContext, (WhichOneVideoPractice) practice);
            case TRANSLATE_VIDEO:
                return new TranslateVideoPracticeView(mContext, (TranslateVideoPractice) practice);
            case DISCOVER_IMAGE:
                return new DiscoverImagePracticeView(mContext, (DiscoverImagePractice) practice);
            case TAKE_PICTURE:
                return new TakePicturePracticeView((Activity)mContext, (TakePicturePractice) practice);

        }
        throw new UnsupportedOperationException(
                "Quiz of type " + practice.getType() + " can not be displayed.");
    }



}
