/*
 * V 0.1
 */
package com.diezsiete.lscapp.ui.widget.wordselector;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diezsiete.lscapp.R;

import java.util.ArrayList;
import java.util.List;


public class WordSelector {


    private LayoutInflater mInflater;

    final private WordSelectorLayout mGroup;

    private final SparseArray<TextView> mTokens;
    private final SparseArray<TextView> mTokensPhantom;

    private SparseIntArray mOptionsSel;

    private AutoTransition mTransition;

    private Scene mScene1;

    private WordSelection mListener;

    private boolean userClick = false;

    private TextView createTokenPhantom(String text, ViewGroup viewGroup) {
        TextView tokenPhantom = (TextView) mInflater.inflate(R.layout.word_selector_token, viewGroup, false);
        if(mGroup.isDebugDraw()){
            tokenPhantom.setBackgroundColor(mInflater.getContext().getResources().getColor(R.color.colorAccent));
        }else
            tokenPhantom.setVisibility(View.INVISIBLE);
        tokenPhantom.setText(text);
        return tokenPhantom;
    }

    private TextView createTokenSeparator(String text, ViewGroup viewGroup) {
        TextView tokenPhantom = (TextView) mInflater.inflate(R.layout.word_selector_token_separator, viewGroup, false);
        if(mGroup.isDebugDraw()){
            tokenPhantom.setBackgroundColor(mInflater.getContext().getResources().getColor(R.color.colorAccent));
        }else
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
        }
    }

    private TextView createToken(String text, int id, ViewGroup viewGroup) {
        TextView viewToken  = (TextView) mInflater.inflate(R.layout.word_selector_token, viewGroup, false);
        viewToken.setText(text);
        viewToken.setId(id);
        viewToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClick = true;
                mGroup.setOnClickTokenActive(true);
                modifyOptionsSel(view.getId());
                addTokens();
                TransitionManager.go(mScene1, mTransition);
                userClick = false;
            }
        });
        return viewToken;
    }

    private void setNewLine(TextView token, boolean setNewLine){
        WordSelectorFlowLayout.LayoutParams params = (WordSelectorFlowLayout.LayoutParams) token.getLayoutParams();
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
        ViewGroup tokensGroup = mGroup;
        SparseArray<TextView> tokensPhantom = mTokensPhantom;
        SparseArray<TextView> tokens = mTokens;

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
            TextView token = tokensPhantom.get(id);
            if(mOptionsSel.indexOfValue(id) < 0) {
                if(token.getTag() == null)
                    setNewLine(token, false);
                tokensGroup.addView(token);
            }
        }

        for(int i = 0, sparseSize = tokens.size(); i < sparseSize; i++){
            int id = tokens.keyAt(i);
            TextView token = tokens.get(id);
            if(mOptionsSel.indexOfValue(id) >= 0) {
                token = tokensPhantom.get(id);
            }
            if(i == 0){
                setNewLine(token, true);
            }
            tokensGroup.addView(token);
        }
        callListener();
    }

    private void createBackLines(int[] tops) {
        Drawable[] drawables = new Drawable[tops.length];
        for(int i = 0; i < tops.length; i++){
            drawables[i] = mInflater.getContext().getResources().getDrawable(R.drawable.word_selector_back);
        }
        LayerDrawable ld = new LayerDrawable(drawables);
        for(int i = 0; i < tops.length; i++)
            ld.setLayerInset(i, 0, tops[i] - 9, 0,0);

        mGroup.setBackground(ld);
    }

    private void callListener() {
        if(userClick) {
            List<Integer> arrayList = new ArrayList<>(mOptionsSel.size());
            for (int i = 0, sparseSize = mOptionsSel.size(); i < sparseSize; i++)
                arrayList.add(mOptionsSel.valueAt(i) - 1);
            mListener.onSelection(arrayList);
        }
    }


    public WordSelector(WordSelectorView sceneRoot, WordSelection listener) {
        mInflater = LayoutInflater.from(sceneRoot.getContext());

        mGroup = (WordSelectorLayout) mInflater.inflate(R.layout.word_selector_scene, sceneRoot, false);

        mScene1 = new Scene(mGroup);

        mTokens = new SparseArray<>();
        mTokensPhantom = new SparseArray<>();
        mOptionsSel = new SparseIntArray();

        mTransition = new AutoTransition();
        mTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
        mTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) { }

            @Override
            public void onTransitionEnd(Transition transition) {
                mGroup.setOnClickTokenActive(false);
            }
            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) { }

            @Override
            public void onTransitionResume(Transition transition) { }
        });

        mListener = listener;

        sceneRoot.addView(mGroup);
    }

    public void setOptions(final List<String> options) {
        mTokens.clear();
        mTokensPhantom.clear();
        for(int i = 1; i <= options.size(); i++){
            mTokensPhantom.append(i, createTokenPhantom(options.get(i-1), mGroup));
            mTokens.append(i, createToken(options.get(i-1), i,  mGroup));
        }
        TextView mSeparator = createTokenSeparator("Separator", mGroup);
        mSeparator.setTag("SEPARATOR");
        setNewLine(mSeparator, true);
        mTokensPhantom.append(options.size() + 1, mSeparator);

        mGroup.setTokensSize(options.size());

        mGroup.setLayoutListener(new WordSelectorLayout.LayoutListener() {
            @Override
            public void onChange() {
                int[] linesTop = new int[mGroup.getLineSize()];
                int lineThickness = 0;
                for(int i = 0; i < mGroup.getLineSize(); i++){
                    lineThickness += mGroup.lines.get(i).getLineThickness();
                    linesTop[i] = lineThickness;
                }
                createBackLines(linesTop);
            }
        });

        addTokens();
    }

    public interface WordSelection {
        public void onSelection(List<Integer> words);
    }

}
