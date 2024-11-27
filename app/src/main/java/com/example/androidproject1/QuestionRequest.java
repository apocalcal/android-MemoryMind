package com.example.androidproject1;

// 요청 데이터 클래스
public class QuestionRequest {
    private int user_id;
    private String question;
    private String answer;
    private String[] options; // 객관식 질문의 선택지 추가

    public QuestionRequest(int user_id, String question, String answer, String[] options) {
        this.user_id = user_id;
        this.question = question;
        this.answer = answer;
        this.options = options;
    }

    // Getter와 Setter 추가
}


