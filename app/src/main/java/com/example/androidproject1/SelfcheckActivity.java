package com.example.androidproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;

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
    private Button selectedButton = null; // 현재 선택된 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfcheck);

        // XML ID 연결
        subtitleTextView = findViewById(R.id.subtitle);
        Button option1 = findViewById(R.id.option1);
        Button option2 = findViewById(R.id.option2);
        Button option3 = findViewById(R.id.option3);
        Button nextBtn = findViewById(R.id.nextBtn);
        Button resultBtn = findViewById(R.id.resultBtn);

        // 초기화 - 다음 버튼 비활성화
        nextBtn.setEnabled(false);

        // 첫 질문 설정
        updateQuestion(option1, option2, option3);

        // 옵션 버튼 클릭 이벤트 처리
        View.OnClickListener answerClickListener = v -> {
            // 선택된 버튼 처리
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(Color.GRAY); // 이전 선택 버튼 색상 복구
            }
            selectedButton = (Button) v;
            selectedButton.setBackgroundColor(Color.parseColor("#0C3155")); // 선택된 버튼 색상 변경

            // "다음" 버튼 활성화
            nextBtn.setEnabled(true);
        };

        option1.setOnClickListener(answerClickListener);
        option2.setOnClickListener(answerClickListener);
        option3.setOnClickListener(answerClickListener);

        // "다음" 버튼 클릭 이벤트 처리
        nextBtn.setOnClickListener(v -> {
            if (selectedButton != null) {
                // 선택된 답변 처리
                String selectedAnswer = selectedButton.getText().toString();
                totalScore += calculateScore(selectedAnswer);

                // 다음 질문으로 이동
                if (currentQuestionIndex < questionAnswers.length - 1) {
                    currentQuestionIndex++;
                    updateQuestion(option1, option2, option3);
                    nextBtn.setEnabled(false); // 다음 버튼 다시 비활성화
                    selectedButton = null; // 선택 초기화
                } else {
                    // 모든 질문이 끝난 경우 처리
                    subtitleTextView.setText("자가진단이 완료되었습니다.");
                    option1.setVisibility(View.GONE);
                    option2.setVisibility(View.GONE);
                    option3.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.GONE);
                    resultBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        // "결과 확인하기" 버튼 클릭
        resultBtn.setOnClickListener(v -> {
            String resultMessage = getResultMessage(totalScore);
            Intent intent = new Intent(SelfcheckActivity.this, ResultActivity.class);
            intent.putExtra("RESULT_MESSAGE", resultMessage);
            startActivity(intent);
        });
    }

    private void updateQuestion(Button option1Button, Button option2Button, Button option3Button) {
        subtitleTextView.setText((currentQuestionIndex + 1) + ". " + questionAnswers[currentQuestionIndex][0]);

        option1Button.setText(questionAnswers[currentQuestionIndex][1]);
        option1Button.setBackgroundColor(Color.GRAY); // 초기화
        option1Button.setVisibility(View.VISIBLE);

        option2Button.setText(questionAnswers[currentQuestionIndex][2]);
        option2Button.setBackgroundColor(Color.GRAY); // 초기화
        option2Button.setVisibility(View.VISIBLE);

        if (questionAnswers[currentQuestionIndex].length > 3) {
            option3Button.setText(questionAnswers[currentQuestionIndex][3]);
            option3Button.setBackgroundColor(Color.GRAY); // 초기화
            option3Button.setVisibility(View.VISIBLE);
        } else {
            option3Button.setVisibility(View.GONE);
        }
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