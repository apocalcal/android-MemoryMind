package com.example.androidproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TodayActivity extends AppCompatActivity {

    private List<String> questions = Arrays.asList(
            "경찰서로 연결되는 번호는 무엇일까요?",
            "응급상황 시 병원에 연결하려면 어디로 전화해야 하나요?",
            "1년은 며칠인가요?",
            "하루는 몇 시간일까요?",
            "해는 아침에 어느 방향에서 뜰까요?",
            "강아지의 다리는 몇 개일까요?",
            "상대방에게 전화하기 위해 사용하는 물건은 무엇인가요?",
            "옷을 빨래하기 위해 사용하는 제품은 무엇인가요?"
    );

    private List<List<String>> options = Arrays.asList(
            Arrays.asList("114", "119", "112"),
            Arrays.asList("114 ", "119", "112"),
            Arrays.asList("12시간", "20시간", "24시간"),
            Arrays.asList("30분", "45분", "60분"),
            Arrays.asList("남쪽", "북쪽", "동쪽"),
            Arrays.asList("6개", "3개", "4개"),
            Arrays.asList("절구", "전자레인지", "휴대전화"),
            Arrays.asList("전구", "스피커", "세탁기")
    );

    private List<Integer> correctAnswers = Arrays.asList(
            2, // 112
            1, // 119
            2, // 24시간
            2, // 60분
            2, // 동쪽
            2, // 4개
            2, // 휴대전화
            2  // 세탁기
    );

    private int QuestionIndex = 0;
    private int score = 0;
    private List<Integer> incorrectQuestions = new ArrayList<>(); // 틀린 문제 기록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        TextView questionText = findViewById(R.id.questionText);
        RadioGroup answerGroup = findViewById(R.id.answerGroup);
        Button nextButton = findViewById(R.id.nextButton);

        // 초기 질문 및 보기 세팅
        updateQuestion(questionText, answerGroup);

        nextButton.setOnClickListener(v -> {
            // 정답 선택 확인
            int selectedId = answerGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(TodayActivity.this, "정답을 선택해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 사용자가 선택한 답의 인덱스 가져오기
            int selectedAnswerIndex = answerGroup.indexOfChild(findViewById(selectedId));

            // 정답 비교
            if (selectedAnswerIndex == correctAnswers.get(QuestionIndex)) {
                score++; // 정답일 경우 점수 증가
            } else {
                incorrectQuestions.add(QuestionIndex); // 틀린 문제 기록
            }

            // 다음 질문으로 이동
            if (QuestionIndex < questions.size() - 1) {
                QuestionIndex++;
                updateQuestion(questionText, answerGroup);
            } else {
                // 모든 질문 완료 - 결과 화면 표시
                showIncorrectAnswers();
                nextButton.setEnabled(false); // 버튼 비활성화
            }
        });
    }

    private void updateQuestion(TextView questionText, RadioGroup answerGroup) {
        // 질문 업데이트
        questionText.setText(questions.get(QuestionIndex));

        // 보기 업데이트
        answerGroup.removeAllViews();
        for (String option : options.get(QuestionIndex)) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            answerGroup.addView(radioButton);
        }
    }

    private void showIncorrectAnswers() {
        // 틀린 문제만 표시
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("점수: ").append(score).append("/").append(questions.size()).append("\n\n");

        if (incorrectQuestions.isEmpty()) {
            resultMessage.append("모든 문제를 맞췄습니다! 축하합니다!");
        } else {
            resultMessage.append("틀린 문제:\n");
            for (int index : incorrectQuestions) {
                resultMessage.append("문제 ").append(index + 1).append(": ").append(questions.get(index)).append("\n");
                resultMessage.append("  정답: ").append(options.get(index).get(correctAnswers.get(index))).append("\n\n");
            }
        }

        // 결과를 TextView에 표시
        TextView questionText = findViewById(R.id.questionText);
        questionText.setText(resultMessage.toString());

        // 보기 삭제
        RadioGroup answerGroup = findViewById(R.id.answerGroup);
        answerGroup.removeAllViews();
    }
}
