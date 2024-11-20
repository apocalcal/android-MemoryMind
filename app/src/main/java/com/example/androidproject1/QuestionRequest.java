package com.example.androidproject1;

// 요청 데이터 클래스
public class QuestionRequest {
    private int user_id;
    private String question;
    private String answer;

    public QuestionRequest(int user_id, String question, String answer) {
        this.user_id = user_id;
        this.question = question;
        this.answer = answer;
    }

    // Getter와 Setter (생략 가능)
}

