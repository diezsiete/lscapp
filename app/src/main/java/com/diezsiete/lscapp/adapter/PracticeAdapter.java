package com.diezsiete.lscapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.widget.practice.AbsPracticeView;
import com.diezsiete.lscapp.widget.practice.DiscoverImagePracticeView;
import com.diezsiete.lscapp.widget.practice.ShowSignPracticeView;
import com.diezsiete.lscapp.widget.practice.TakePicturePracticeView;
import com.diezsiete.lscapp.widget.practice.TranslateVideoPracticeView;
import com.diezsiete.lscapp.widget.practice.WhichOneVideoPracticeView;
import com.diezsiete.lscapp.widget.practice.WhichOneVideosPracticeView;




public class PracticeAdapter extends BaseAdapter{

    private final Context mContext;
    private final String mLevelId;
    private Practice[] mData;

    public PracticeAdapter(Context context, String levelId) {
        mContext = context;
        mLevelId = levelId;
    }

    @Override
    public int getCount() {
        if(mData == null)
            return 0;
        return mData.length;
    }

    @Override
    public int getViewTypeCount() {
        return mData == null ? 0 : mData.length;
    }

    @Override
    public Practice getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int i) {
        return mData[i].getId();
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

    public void setData(Practice[] data) {
        mData = data;
        //notifyDataSetChanged();
    }


    private AbsPracticeView getViewInternal(Practice practice) {
        if (null == practice) {
            throw new IllegalArgumentException("Practice must not be null");
        }
        return createViewFor(practice);
    }

    private AbsPracticeView createViewFor(Practice practice) {
        switch (practice.getType()) {
            case "show-sign":
                return new ShowSignPracticeView(mContext, practice);
            case "which-one-videos":
                return new WhichOneVideosPracticeView(mContext, practice);
            case "which-one-video":
                return new WhichOneVideoPracticeView(mContext, practice);
            case "translate-video":
                return new TranslateVideoPracticeView(mContext, practice);
            case "discover-image":
                return new DiscoverImagePracticeView(mContext, practice);
            case "take-picture":
                return new TakePicturePracticeView(mContext, practice);

        }
        throw new UnsupportedOperationException(
                "Quiz of type " + practice.getType() + " can not be displayed.");
    }



}
