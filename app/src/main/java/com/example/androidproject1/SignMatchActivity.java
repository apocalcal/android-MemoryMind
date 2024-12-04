package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignMatchActivity extends AppCompatActivity {

    private LinearLayout verticalLayout;
    private TextView instructionText, timerText;
    private Button submitButton, nextButton;
    private List<SignItem> signsList;
    private int currentQuestion = 0;
    private int score = 0;
    private final int signsPerQuestion = 3; // 문제당 표지판 개수
    private final int totalQuestions = 3;  // 전체 문제 수
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signmatch);

        // 뷰 초기화
        verticalLayout = findViewById(R.id.verticalLayout);
        instructionText = findViewById(R.id.instructionText);
        submitButton = findViewById(R.id.submitButton);
        nextButton = findViewById(R.id.nextButton);

        timerText = new TextView(this); // 타이머 텍스트 추가
        timerText.setTextSize(20);
        timerText.setVisibility(View.GONE);
        verticalLayout.addView(timerText);

        instructionText.setText("표지판 문제를 풀어보세요!");

        initializeSigns();
        startGame();
    }

    private void initializeSigns() {
        signsList = new ArrayList<>();
        signsList.add(new SignItem("어린이 보호구역", "sign01"));
        signsList.add(new SignItem("횡단보도", "sign02"));
        signsList.add(new SignItem("자전거 전용도로", "sign03"));
        signsList.add(new SignItem("횡단금지", "sign04"));
        signsList.add(new SignItem("통행금지", "sign05"));
        signsList.add(new SignItem("자전거 통행금지", "sign06"));
        signsList.add(new SignItem("정지", "sign07"));
        signsList.add(new SignItem("위험", "sign08"));
        signsList.add(new SignItem("시속 30km제한", "sign09"));
        signsList.add(new SignItem("정지", "sign10"));
        signsList.add(new SignItem("유턴금지", "sign11"));
        signsList.add(new SignItem("주차금지", "sign12"));
        signsList.add(new SignItem("주정차금지", "sign13"));
        signsList.add(new SignItem("보행자 전용도로", "sign14"));
        signsList.add(new SignItem("시속 10km제한", "sign15"));
        signsList.add(new SignItem("머리조심", "sign16"));
        signsList.add(new SignItem("보행금지", "sign17"));
        signsList.add(new SignItem("출입금지", "sign18"));
        Collections.shuffle(signsList);
    }

    private void startGame() {
        if (currentQuestion >= totalQuestions) {
            showFinalResult();
            return;
        }

        currentQuestion++;
        displaySigns();
    }

    private void displaySigns() {
        verticalLayout.removeAllViews();
        verticalLayout.addView(timerText);

        int startIndex = (currentQuestion - 1) * signsPerQuestion;
        int endIndex = Math.min(currentQuestion * signsPerQuestion, signsList.size());

        if (startIndex >= signsList.size()) {
            Log.e("SignMatchActivity", "현재 표지판을 불러올 수 없습니다.");
            return;
        }

        List<SignItem> currentSigns = signsList.subList(startIndex, endIndex);

        List<TextView> answerTexts = new ArrayList<>();
        List<EditText> answerInputs = new ArrayList<>();
        List<TextView> correctAnswerViews = new ArrayList<>();

        for (SignItem sign : currentSigns) {
            LinearLayout signLayout = new LinearLayout(this);
            signLayout.setOrientation(LinearLayout.VERTICAL);
            signLayout.setPadding(20, 20, 20, 20);

            // 표지판 이미지
            ImageView signImage = new ImageView(this);
            int resId = getResources().getIdentifier(sign.getImageName(), "drawable", getPackageName());
            signImage.setImageResource(resId);

            // 이미지 크기 및 정렬 설정
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(300, 300);
            imageParams.setMargins(10, 10, 10, 20);
            signImage.setLayoutParams(imageParams);
            signImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            signLayout.addView(signImage);

            // 정답 텍스트 (초기 표시)
            TextView answerText = new TextView(this);
            answerText.setText(sign.getAnswer());
            answerText.setTextSize(18);
            answerText.setTypeface(getResources().getFont(R.font.hakgyoansimb)); // 폰트 설정
            answerText.setVisibility(View.VISIBLE); // 초기 표시
            signLayout.addView(answerText);
            answerTexts.add(answerText);

            // 정답 입력란 (초기 숨김)
            EditText answerInput = new EditText(this);
            answerInput.setHint("정답 입력");
            answerInput.setTextSize(18);
            answerInput.setTypeface(getResources().getFont(R.font.hakgyoansimr)); // 폰트 설정
            answerInput.setVisibility(View.GONE); // 초기 숨김
            signLayout.addView(answerInput);
            answerInputs.add(answerInput);

            // 정답/오답 확인 텍스트 (초기 숨김)
            TextView correctAnswerView = new TextView(this);
            correctAnswerView.setVisibility(View.GONE);
            correctAnswerView.setTextSize(18);
            correctAnswerView.setTypeface(getResources().getFont(R.font.hakgyoansimb)); // 폰트 설정
            signLayout.addView(correctAnswerView);
            correctAnswerViews.add(correctAnswerView);

            verticalLayout.addView(signLayout);
        }

        setupButtons(answerTexts, answerInputs, currentSigns, correctAnswerViews);
    }



    private void setupButtons(List<TextView> answerTexts, List<EditText> answerInputs, List<SignItem> currentSigns, List<TextView> correctAnswerViews) {
        submitButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

        // 타이머로 정답 가리기
        timerText.setVisibility(View.VISIBLE);
        handler.post(new Runnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown > 0) {
                    timerText.setText("남은 시간: " + countdown + "초");
                    countdown--;
                    handler.postDelayed(this, 1000);
                } else {
                    timerText.setVisibility(View.GONE);
                    for (TextView answerText : answerTexts) {
                        answerText.setVisibility(View.GONE); // 정답 숨김
                    }
                    for (EditText answerInput : answerInputs) {
                        answerInput.setVisibility(View.VISIBLE); // 입력란 표시
                    }
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });

        submitButton.setOnClickListener(v -> {
            int correctCount = 0;

            for (int i = 0; i < currentSigns.size(); i++) {
                SignItem sign = currentSigns.get(i);
                String userAnswer = answerInputs.get(i).getText().toString().trim();

                if (userAnswer.equals(sign.getAnswer())) {
                    correctAnswerViews.get(i).setText("정답입니다!");
                    correctCount++;
                } else {
                    correctAnswerViews.get(i).setText("정답: " + sign.getAnswer());
                }

                correctAnswerViews.get(i).setVisibility(View.VISIBLE);
                answerInputs.get(i).setVisibility(View.GONE);
            }

            score += correctCount;

            submitButton.setVisibility(View.GONE);
            nextButton.setText(currentQuestion == totalQuestions ? "결과 보기" : "다음");
            nextButton.setVisibility(View.VISIBLE);
        });

        nextButton.setOnClickListener(v -> {
            if (currentQuestion == totalQuestions) {
                showFinalResult();
            } else {
                startGame();
            }
        });
    }


    private void showFinalResult() {
        try {
            Intent intent = new Intent(SignMatchActivity.this, MatchResultActivity.class);

            // 점수와 총 문제 수를 Intent에 추가
            intent.putExtra("SCORE", score);
            intent.putExtra("TOTAL_SIGNS", totalQuestions * signsPerQuestion);

            // 로깅 추가 (디버깅용)
            Log.d("SignMatchActivity", "결과 보기로 이동: SCORE=" + score + ", TOTAL_SIGNS=" + (totalQuestions * signsPerQuestion));

            startActivity(intent);
            finish();
        } catch (Exception e) {
            // 튕김 현상 디버깅용 로그
            Log.e("SignMatchActivity", "결과 보기 이동 중 오류 발생", e);
        }
    }

    private static class SignItem {
        private final String answer;
        private final String imageName;

        public SignItem(String answer, String imageName) {
            this.answer = answer;
            this.imageName = imageName;
        }

        public String getAnswer() {
            return answer;
        }

        public String getImageName() {
            return imageName;
        }
    }
}
