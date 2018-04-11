package com.diezsiete.lscapp.widget;

/**
 * V 0.1
 */

import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diezsiete.lscapp.R;

import org.apmem.tools.layouts.FlowLayout;


public class WordSelector {


    private LayoutInflater mInflater;

    final private ViewGroup mGroup;
    final private ViewGroup mSelGroup;

    private Scene mScene1;
    private Scene mScene2;

    private final SparseArray<TextView> mTokens;
    private final SparseArray<TextView> mTokensPhantom;
    private final SparseArray<TextView> mTokens2;
    private final SparseArray<TextView> mTokensPhantom2;

    private SparseIntArray mOptionsSel;

    private boolean scene1 = true;


    private TextView createTokenPhantom(String text, int id, ViewGroup viewGroup) {
        TextView tokenPhantom = (TextView) mInflater.inflate(R.layout.word_selector_token, viewGroup, false);
        tokenPhantom.setVisibility(View.INVISIBLE);
        tokenPhantom.setText(text);
        return tokenPhantom;
    }

    private void modifyOptionsSel(int viewId) {
        if(mOptionsSel.indexOfValue(viewId) < 0) {
            int key = mOptionsSel.size() == 0 ? 0 : mOptionsSel.keyAt(mOptionsSel.size() - 1) + 1;
            mOptionsSel.append(key, viewId);
        }else {
            mOptionsSel.removeAt(mOptionsSel.indexOfValue(viewId));
            //segunda opcion que implica que append sea key = mOptionsSel.size()
            /*if(mOptionsSel.size() > 0) {
                SparseIntArray temp = new SparseIntArray(mOptionsSel.size());
                for (int i = 0, sparseSize = mOptionsSel.size(); i < sparseSize; i++) {
                    temp.append(i, mOptionsSel.valueAt(i));
                }
                mOptionsSel = temp;
            }*/
        }
    }

    private TextView createToken(String text, int id, ViewGroup viewGroup) {
        TextView viewToken  = (TextView) mInflater.inflate(R.layout.word_selector_token, viewGroup, false);
        viewToken.setText(text);
        viewToken.setId(id);
        viewToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scene1 = !scene1;
                modifyOptionsSel(view.getId());
                addTokens();
                TransitionManager.go(scene1 ? mScene1 : mScene2);
            }
        });
        return viewToken;
    }

    private void setNewLine(TextView token, boolean setNewLine){
        FlowLayout.LayoutParams params = (FlowLayout.LayoutParams) token.getLayoutParams();
        params.setNewLine(setNewLine);
        token.setLayoutParams(params);
    }

    private void removeParentsTokens(SparseArray<TextView> tokens) {
        for(int i = 0, sparseSize = tokens.size(); i < sparseSize; i++){
            TextView token = tokens.get(tokens.keyAt(i));
            if(token.getParent() != null) {
                ViewGroup parent = (ViewGroup) token.getParent();
                parent.removeView(token);
            }
        }
    }

    private void addTokens() {
        ViewGroup tokensGroup = scene1 ? mGroup : mSelGroup;
        SparseArray<TextView> tokensPhantom = scene1 ? mTokensPhantom : mTokensPhantom2;
        SparseArray<TextView> tokens = scene1 ? mTokens : mTokens2;

        removeParentsTokens(tokensPhantom);
        removeParentsTokens(tokens);


        for(int i = 0, sparseSize = mOptionsSel.size(); i < sparseSize; i++) {
            int id = mOptionsSel.valueAt(i);
            TextView token = tokens.get(id);
            if(id == 1)
                setNewLine(token, false);
            tokensGroup.addView(tokens.get(id));
        }

        for(int i = 0, sparseSize = tokensPhantom.size(); i < sparseSize; i++){
            int id = tokensPhantom.keyAt(i);
            if(mOptionsSel.indexOfValue(id) < 0) {
                tokensGroup.addView(tokensPhantom.get(id));
            }
        }

        for(int i = 0, sparseSize = tokens.size(); i < sparseSize; i++){
            int id = tokens.keyAt(i);
            TextView token = null;
            if(mOptionsSel.indexOfValue(id) >= 0){
                token = tokensPhantom.get(id);
            }else{
                token = tokens.get(id);
            }
            if(i == 0){
                setNewLine(token, true);
            }
            tokensGroup.addView(token);
        }
    }


    public WordSelector(ViewGroup sceneRoot) {
        mInflater = LayoutInflater.from(sceneRoot.getContext());

        mGroup = (ViewGroup) mInflater.inflate(R.layout.word_selector_scene, sceneRoot, false);
        mSelGroup = (ViewGroup) mInflater.inflate(R.layout.word_selector_scene, sceneRoot, false);

        mScene1 = new Scene(sceneRoot, mGroup);
        mScene2 = new Scene(sceneRoot, mSelGroup);

        mTokens = new SparseArray<>();
        mTokensPhantom = new SparseArray<>();
        mTokens2 = new SparseArray<>();
        mTokensPhantom2 = new SparseArray<>();
        mOptionsSel = new SparseIntArray();

        sceneRoot.addView(mGroup);
    }

    public void setOptions(String[] options) {
        for(int i = 1; i <= options.length; i++){
            mTokensPhantom.append(i, createTokenPhantom(options[i-1], i, mGroup));
            mTokens.append(i, createToken(options[i-1], i, mGroup));
            mTokensPhantom2.append(i, createTokenPhantom(options[i-1], i, mSelGroup));
            mTokens2.append(i, createToken(options[i-1], i, mSelGroup));
        }
        addTokens();
    }

}
