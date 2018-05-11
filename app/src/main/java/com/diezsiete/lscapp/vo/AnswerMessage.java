package com.diezsiete.lscapp.vo;

public class AnswerMessage {
    public static final int SUCCESS = 1;
    public static final int DANGER = 0;

    public int status;
    public String answer;

    public static AnswerMessage success(String answer){
        return new AnswerMessage(AnswerMessage.SUCCESS, answer);
    }

    public static AnswerMessage danger(String answer){
        return new AnswerMessage(AnswerMessage.DANGER, answer);
    }

    private AnswerMessage(int status, String answer) {
       this.status = status;
       this.answer = answer;
    }
}
