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


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Practice)) {
            return false;
        }

        Practice practice = (Practice) o;

        if (mSolved != practice.mSolved) {
            return false;
        }else if (!mQuizType.equals(practice.mQuizType)) {
            return false;
        }

        return true;
    }

    public int getId() {
        return getQuestion().hashCode();
    }
}
