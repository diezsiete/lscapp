package com.diezsiete.lscapp.widget.practice;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.diezsiete.lscapp.activity.PracticeActivity;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.R;

public abstract class AbsPracticeView<Q extends Practice> extends FrameLayout {
    private final Q mPractice;

    private final LayoutInflater mLayoutInflater;

    private boolean mAnswered;
    private ImageButton mSubmitAnswer;

    protected TextView mQuestionView;

    private LinearLayout mContainer;


    /**
     * Enables creation of views for quizzes.
     *
     * @param context The context for this view.
     * @param practice The actual {@link Practice} that is going to be displayed.
     */
    public AbsPracticeView(Context context, Q practice) {
        super(context);
        mPractice = practice;
        mLayoutInflater = LayoutInflater.from(context);

        setId(practice.getId());

        RelativeLayout relativeContainer =
            (RelativeLayout) mLayoutInflater.inflate(R.layout.practice_container, this, false);
        mContainer = relativeContainer.findViewById(R.id.absPracticeViewContainer);

        mSubmitAnswer = relativeContainer.findViewById(R.id.submitAnswer);
        mSubmitAnswer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer(v);
                mSubmitAnswer.setEnabled(false);
            }
        });

        mQuestionView = mContainer.findViewById(R.id.question_view);
        setUpQuestionView();

        View practiceContentView = createPracticeContentView();
        practiceContentView.setId(R.id.practice_content);
        mContainer.addView(practiceContentView, 1);

        addView(relativeContainer);
    }



    public Q getPractice() {
        return mPractice;
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    protected boolean isAnswered() {
        return mAnswered;
    }

    /**
     * Sets the quiz to answered or unanswered.
     *
     * @param answered <code>true</code> if an answer was selected, else <code>false</code>.
     */
    protected void allowAnswer(final boolean answered) {
        if (null != mSubmitAnswer) {
            if (answered) {
                mSubmitAnswer.setVisibility(VISIBLE);
            } else {
                mSubmitAnswer.setVisibility(INVISIBLE);
            }
            mAnswered = answered;
        }
    }

    /**
     * Sets the quiz to answered if it not already has been answered.
     * Otherwise does nothing.
     */
    protected void allowAnswer() {
        if (!isAnswered()) {
            allowAnswer(true);
        }
    }

    /**
     * Allows children to submit an answer via code.
     */
    protected void submitAnswer() {
        submitAnswer(findViewById(R.id.submitAnswer));
    }

    @SuppressWarnings("UnusedParameters")
    private void submitAnswer(final View v) {
        final boolean answerCorrect = isAnswerCorrect();
        //mPractice.setSolved(true);

        if (getContext() instanceof PracticeActivity) {
            ((PracticeActivity) getContext()).proceed();
        }
    }
    /**
     * Sets the behaviour for all question views.
     */
    abstract protected void setUpQuestionView();

    /**
     * Implementations should create the content view for the type of
     * {@link com.diezsiete.lscapp.data.db.model.Practice} they want to display.
     *
     * @return the created view to solve the quiz.
     */
    protected abstract View createPracticeContentView();

    /**
     * Implementations must make sure that the answer provided is evaluated and correctly rated.
     *
     * @return <code>true</code> if the question has been correctly answered, else
     * <code>false</code>.
     */
    protected abstract boolean isAnswerCorrect();

    /**
     * Save the user input to a bundle for orientation changes.
     *
     * @return The bundle containing the user's input.
     */
    public abstract Bundle getUserInput();

    /**
     * Restore the user's input.
     *
     * @param savedInput The input that the user made in a prior instance of this view.
     */
    public abstract void setUserInput(Bundle savedInput);
}
