package com.example.androidproject1;

import java.util.List;

public class Question {
    private String question;
    private String answer;
    private List<String> options;

    public Question(String question, String answer, List<String> options) {
        this.question = question;
        this.answer = answer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getOptions() {
        return options;
    }

    // 객관식의 정답 인덱스 반환
    public int getCorrectAnswerIndex() {
        if (options == null) {
            return -1; // 주관식일 경우
        }
        return options.indexOf(answer); // 정답이 있는 인덱스 반환
    }
}
