package com.example.androidproject1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TodayActivity extends AppCompatActivity {

    private List<String> allQuestions = Arrays.asList(
            "오늘의 날짜는 몇 월 며칠인가요?",
            "경찰서로 연결되는 번호는 무엇일까요?",
            "응급상황 시 병원에 연결하려면 어디로 전화해야 하나요?",
            "1년은 며칠인가요?",
            "1년은 달(月)이 몇 개인가요?",
            "하루는 몇 시간일까요?",
            "한 시간은 몇 분일까요?",
            "1분은 몇 초일까요?"
    );

    private List<List<String>> allOptions = Arrays.asList(
            null, // 날짜 질문은 옵션이 없음
            Arrays.asList("114", "119", "112"),
            Arrays.asList("114", "119", "112"),
            Arrays.asList("356일", "360일", "365일"),
            Arrays.asList("13개", "10개", "12개"),
            Arrays.asList("12시간", "20시간", "24시간"),
            Arrays.asList("30분", "45분", "60분"),
            Arrays.asList("59초", "58초", "60초")
    );

    private List<Integer> allCorrectAnswers = Arrays.asList(
            null, // 날짜 질문은 정답 인덱스 없음
            2,    // 112
            1,    // 119
            2,    // 365일
            2,     // 12개
            2,  // 24시간
            2,  // 60분
            2   // 60초

    );

    private List<Integer> incorrectQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        TextView questionText = findViewById(R.id.questionText);
        RadioGroup answerGroup = findViewById(R.id.answerGroup);
        DatePicker datePicker = findViewById(R.id.datePicker);
        Button nextBtn = findViewById(R.id.nextBtn);

        updateQuestion(questionText, answerGroup, datePicker);

        nextBtn.setOnClickListener(v -> handleNextQuestion(answerGroup, datePicker, questionText, nextBtn));
    }

    private void updateQuestion(TextView questionText, RadioGroup answerGroup, DatePicker datePicker) {
        int questionIndex = currentQuestionIndex;
        questionText.setText(allQuestions.get(questionIndex));

        if (questionIndex == 0) { // 날짜 질문일 경우
            datePicker.setVisibility(View.VISIBLE);
            answerGroup.setVisibility(View.GONE);
            datePicker.updateDate(2000,0,5);
        } else {
            datePicker.setVisibility(View.GONE);
            answerGroup.setVisibility(View.VISIBLE);

            answerGroup.removeAllViews();
            for (String option : allOptions.get(questionIndex)) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioButton.setTextSize(20);
                answerGroup.addView(radioButton);
            }
        }
    }

    private void handleNextQuestion(RadioGroup answerGroup, DatePicker datePicker, TextView questionText, Button nextBtn) {
        int questionIndex = currentQuestionIndex;
        boolean isCorrect = false;

        if (questionIndex == 0) { // 날짜 질문 처리
            Calendar today = Calendar.getInstance();
            int selectedDay = datePicker.getDayOfMonth();
            int selectedMonth = datePicker.getMonth();
            int selectedYear = datePicker.getYear();

            isCorrect = (selectedDay == today.get(Calendar.DAY_OF_MONTH) &&
                    selectedMonth == today.get(Calendar.MONTH) &&
                    selectedYear == today.get(Calendar.YEAR));
        } else { // 일반 질문 처리
            int selectedId = answerGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                questionText.setText("정답을 선택해주세요!");
                return;
            }

            int selectedAnswerIndex = answerGroup.indexOfChild(findViewById(selectedId));
            isCorrect = (selectedAnswerIndex == allCorrectAnswers.get(questionIndex));
        }

        if (isCorrect) {
            score++;
        } else {
            incorrectQuestions.add(questionIndex); // 틀린 문제 기록
        }

        // 다음 질문 또는 결과 표시
        if (currentQuestionIndex < allQuestions.size() - 1) {
            currentQuestionIndex++;
            updateQuestion(questionText, answerGroup, datePicker);
        } else {
            showResults(questionText, answerGroup, datePicker, nextBtn); // 결과 표시
        }
    }

    private void showResults(TextView questionText, RadioGroup answerGroup, DatePicker datePicker, Button nextBtn) {
        datePicker.setVisibility(View.GONE);
        answerGroup.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE); // 다음 버튼 숨기기

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("퀴즈 완료!\n점수: ").append(score).append("/").append(allQuestions.size()).append("\n\n");

        if (incorrectQuestions.isEmpty()) {
            resultMessage.append("모든 문제를 맞췄습니다! 축하합니다!");
        } else {
            resultMessage.append("틀린 문제:\n");
            for (int questionIndex : incorrectQuestions) {
                resultMessage.append("- ").append(allQuestions.get(questionIndex)).append("\n");
                if (allOptions.get(questionIndex) != null) { // 일반 질문의 정답
                    resultMessage.append("  정답: ").append(allOptions.get(questionIndex).get(allCorrectAnswers.get(questionIndex))).append("\n\n");
                } else { // 날짜 질문의 정답
                    Calendar today = Calendar.getInstance();
                    resultMessage.append("  정답: ").append(today.get(Calendar.MONTH) + 1)
                            .append("월 ")
                            .append(today.get(Calendar.DAY_OF_MONTH))
                            .append("일\n\n");
                }
            }
        }

        questionText.setText(resultMessage.toString());
    }
}
