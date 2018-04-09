package com.diezsiete.lscapp.model.practice;



public abstract class Practice {
    private static final String TAG = "Practice";
    private final String mQuizType;
    private boolean mSolved = false;

    protected Practice() {
        mQuizType = getType().getJsonName();
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isSolved() {
        return mSolved;
    }
    /**
     * @return The {@link PracticeType} that represents this quiz.
     */
    public abstract PracticeType getType();

    public abstract String getQuestion();
}
