package com.example.androidproject1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
            2,    // 12개
            2,    // 24시간
            2,    // 60분
            2     // 60초
    );
    private List<Integer> incorrectQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedAnswerIndex = -1; // 최종 선택된 답변

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        TextView questionText = findViewById(R.id.questionText);
        ViewGroup buttonContainer = findViewById(R.id.buttonContainer);
        DatePicker datePicker = findViewById(R.id.datePicker);
        Button nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(v -> {
            if (currentQuestionIndex < allQuestions.size() - 1) {
                processAnswer();
                currentQuestionIndex++;
                updateQuestion(questionText, buttonContainer, datePicker, nextBtn);
            } else {
                processAnswer();
                showResults(questionText, buttonContainer, datePicker, nextBtn);
            }
        });

        updateQuestion(questionText, buttonContainer, datePicker, nextBtn);
    }

    private void updateQuestion(TextView questionText, ViewGroup buttonContainer, DatePicker datePicker, Button nextBtn) {
        int questionIndex = currentQuestionIndex;
        questionText.setText(allQuestions.get(questionIndex));
        buttonContainer.removeAllViews(); // 기존 버튼 제거
        selectedAnswerIndex = -1; // 선택 초기화
        if (questionIndex == 0) { // 날짜 질문일 경우
            datePicker.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.GONE);
            nextBtn.setEnabled(true); // 날짜 질문에서는 항상 활성화
        } else {
            datePicker.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.VISIBLE);
            nextBtn.setEnabled(false); // 정답 선택 전까지 비활성화
            // 선택지 버튼 생성
            List<Button> optionButtons = new ArrayList<>();
            for (int i = 0; i < allOptions.get(questionIndex).size(); i++) {
                String option = allOptions.get(questionIndex).get(i);
                final int index = i;
                Button button = new Button(this);
                button.setText(option);
                button.setTextSize(20);
                Typeface newfont = ResourcesCompat.getFont(this, R.font.hakgyoansimb);
                button.setTypeface(newfont);
                // 버튼 간 간격 설정
                ViewGroup.MarginLayoutParams setlayout = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, // 너비를 부모에 맞춤
                        ViewGroup.LayoutParams.WRAP_CONTENT // 높이는 내용에 맞춤
                );
                setlayout.setMargins(0, 10, 0, 10); // 위, 오른쪽, 아래, 왼쪽 마진 설정
                button.setLayoutParams(setlayout);
                button.setOnClickListener(v -> {
                    // 선택한 버튼의 색상을 변경하고, 다른 버튼은 초기화
                    for (Button btn : optionButtons) {
                        btn.setBackgroundColor(Color.LTGRAY); // 초기 색상
                    }
                    button.setBackgroundColor(Color.CYAN); // 선택된 버튼 강조
                    selectedAnswerIndex = index; // 선택된 답변 저장
                    nextBtn.setEnabled(true); // 다음 버튼 활성화
                });
                button.setBackgroundColor(Color.LTGRAY); // 초기 색상
                buttonContainer.addView(button);
                optionButtons.add(button);
            }
        }
    }

    private void processAnswer() {
        int questionIndex = currentQuestionIndex;
        if (questionIndex > 0 && selectedAnswerIndex != -1) {
            if (selectedAnswerIndex == allCorrectAnswers.get(questionIndex)) {
                score++;
            } else {
                incorrectQuestions.add(questionIndex); // 틀린 문제 기록
            }
        }
    }

    private void showResults(TextView questionText, ViewGroup buttonContainer, DatePicker datePicker, Button nextBtn) {
        datePicker.setVisibility(View.GONE);
        buttonContainer.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("퀴즈 완료!\n점수: ").append(score).append("/").append(allQuestions.size()).append("\n\n");
        if (incorrectQuestions.isEmpty()) {
            resultMessage.append("모든 문제를 맞췄습니다! 축하합니다!");
        } else {
            resultMessage.append("틀린 문제:\n");
            for (int questionIndex : incorrectQuestions) {
                resultMessage.append("- ").append(allQuestions.get(questionIndex)).append("\n");
                if (allOptions.get(questionIndex) != null) {
                    resultMessage.append("  정답: ").append(allOptions.get(questionIndex).get(allCorrectAnswers.get(questionIndex))).append("\n\n");
                }
            }
        }
        questionText.setText(resultMessage.toString());
    }
}
