package com.example.androidproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

public class SelfcheckActivity extends AppCompatActivity {

    private String[][] questionAnswers = {
            {"오늘이 몇 월이고, 무슨 요일인지 잘 모른다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"자기가 놔둔 물건을 찾지 못한다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"같은 질문을 반복해서 한다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"약속하고서 잊어버린다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"물건을 가지러 갔다가 잊어버리고 그냥 온다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"물건이나, 사람의 이름을 대기가 힘들어 머뭇거린다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"대화 중 내용이 이해되지 않아 반복해서 물어본다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"길을 잃거나 헤맨 적이 있다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"예전에 비해서 계산 능력이 떨어졌다.(물건 값이나 거스름 돈 계산을 못한다.)", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"예전에 비해 성격이 변했다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"이전에 잘 다루던 가구의 사용이 서툴러졌다.(세탁기, 전기밥솥, 경운기 등)", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"예전에 비해 방이나 집안의 정리 정돈을 못한다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"상황에 맞게 스스로 옷을 선택하여 입지 못한다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"혼자 대중교통 수단을 이용하여 목적지에 가기 힘들다.(신체적인 문제로 인한 것은 제외)", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"내복이나 옷이 더러워져도 갈아입지 않으려고 한다.", "아니다", "가끔 그렇다", "자주 그렇다"},
            {"대체로 활기차게 사시는 편입니까?", "예", "아니오"},
            {"좋지 않은 일이 생길 것 같아 걱정스럽습니까?", "예", "아니오"},
            {"불안해지거나 안절부절 못할 때가 많습니까?", "예", "아니오"},
            {"기분이 가라앉거나 울적할 때가 자주 있습니까?", "예", "아니오"},
            {"산다는 것이 매우 신나고 즐겁습니까?", "예", "아니오"},
            {"자신의 처지가 절망적이라고 느껴지십니까?", "예", "아니오"},
            {"울고 싶을 때가 자주 있습니까?", "예", "아니오"},
            {"집중하기가 어렵습니까?", "예", "아니오"},
            {"아침에 기분 좋게 일어나십니까?", "예", "아니오"},
            {"예전처럼 정신이 맑습니까?", "예", "아니오"}
    };


    private int currentQuestionIndex = 0;
    private int totalScore = 0; // 점수 누적 변수
    private TextView subtitleTextView;
    private RadioGroup answersGroup;
    private RadioButton option1, option2, option3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfcheck);

        // XML ID 연결
        subtitleTextView = findViewById(R.id.subtitle);
        answersGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        Button nextButton = findViewById(R.id.joinBtn);
        Button resultButton = findViewById(R.id.resultButton); // 결과 확인하기 버튼 연결

        // 첫 질문 설정
        updateQuestion();

        // "다음" 버튼 클릭 시
        nextButton.setOnClickListener(v -> {
            // 선택한 답변 확인
            int selectedId = answersGroup.getCheckedRadioButtonId();
            if (selectedId != -1) { // 선택된 답변이 있는 경우
                RadioButton selectedButton = findViewById(selectedId);

                // 점수 계산
                String selectedAnswer = selectedButton.getText().toString();
                totalScore += calculateScore(selectedAnswer);
            }

            if (currentQuestionIndex < questionAnswers.length - 1) {
                currentQuestionIndex++;
                updateQuestion();
            } else {
                // 모든 질문이 끝난 경우 처리
                subtitleTextView.setText("자가진단이 완료되었습니다.");
                option1.setVisibility(View.GONE);
                option2.setVisibility(View.GONE);
                option3.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE); // "다음" 버튼 숨기기
                resultButton.setVisibility(View.VISIBLE); // "결과 확인하기" 버튼 보이기
            }
        });

        // "결과 확인하기" 버튼 클릭 시
        resultButton.setOnClickListener(v -> {
            // 결과 메시지 생성
            String resultMessage = getResultMessage(totalScore);

            // Intent로 ResultActivity 호출
            Intent intent = new Intent(SelfcheckActivity.this, ResultActivity.class);
            intent.putExtra("RESULT_MESSAGE", resultMessage); // 결과 메시지 전달
            startActivity(intent);
        });
    }

    private void updateQuestion() {
        // 현재 질문 설정
        subtitleTextView.setText((currentQuestionIndex + 1) + ". " + questionAnswers[currentQuestionIndex][0]);

        // 첫 번째 옵션
        option1.setText(questionAnswers[currentQuestionIndex][1]);
        option1.setVisibility(View.VISIBLE);

        // 두 번째 옵션
        option2.setText(questionAnswers[currentQuestionIndex][2]);
        option2.setVisibility(View.VISIBLE);

        // 세 번째 옵션 (존재 여부에 따라 가시성 조정)
        if (questionAnswers[currentQuestionIndex].length > 3) {
            option3.setText(questionAnswers[currentQuestionIndex][3]);
            option3.setVisibility(View.VISIBLE);
        } else {
            option3.setVisibility(View.GONE);
        }

        // RadioGroup 초기화
        answersGroup.clearCheck();
    }

    // 점수 계산 메서드
    private int calculateScore(String answer) {
        switch (answer) {
            case "아니다": return 0;
            case "가끔 그렇다": return 1;
            case "자주 그렇다": return 2;
            case "예": return 1;
            case "아니오": return 0;
            default: return 0;
        }
    }

    // 결과 메시지 생성 메서드
    private String getResultMessage(int score) {
        if (score <= 6) {
            return "당신은 치매가 아닙니다.";
        } else if (score <= 18) {
            return "당신은 치매가 의심스럽습니다.";
        } else {
            return "당신은 치매 가능성이 높습니다.";
        }
    }
}