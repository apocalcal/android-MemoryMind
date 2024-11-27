package com.example.androidproject1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ColormatchActivity extends AppCompatActivity {

    private TextView colorText, resultText, scoreText;
    private Button mainMenuButton;
    private LinearLayout buttonContainer;
    private List<String> colorNames;
    private List<Integer> colorValues;
    private int currentColorIndex;
    private Random random;
    private int score = 0;
    private int questionCount = 0;
    private final int totalQuestions = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatch);

        // View 초기화
        colorText = findViewById(R.id.colorText);
        resultText = findViewById(R.id.resultText);
        scoreText = findViewById(R.id.scoreText);
        mainMenuButton = findViewById(R.id.mainMenuButton);
        buttonContainer = findViewById(R.id.buttonContainer);

        // resultText 초기화 확인
        if (resultText == null) {
            Toast.makeText(this, "resultText 뷰를 찾을 수 없습니다. XML 파일을 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 돌아가기 버튼 숨기기 (게임 종료 후에만 표시)
        mainMenuButton.setVisibility(View.GONE);

        // 돌아가기 버튼 클릭 이벤트
        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ColormatchActivity.this, CognitiveActivity.class);
            startActivity(intent);
            finish();
        });

        // 초기화
        colorNames = new ArrayList<>();
        colorValues = new ArrayList<>();
        addInitialColors();
        random = new Random();

        // 첫 번째 문제 설정
        setNewQuestion();
    }

    private void addInitialColors() {
        colorNames.add("빨강");
        colorValues.add(Color.RED);
        colorNames.add("파랑");
        colorValues.add(Color.BLUE);
        colorNames.add("초록");
        colorValues.add(Color.GREEN);
    }

    private void setNewQuestion() {
        if (questionCount >= totalQuestions) {
            showFinalResult();
            return;
        }

        questionCount++;
        int textIndex = random.nextInt(colorNames.size());
        currentColorIndex = random.nextInt(colorValues.size());

        colorText.setText(colorNames.get(textIndex));
        colorText.setTextColor(colorValues.get(currentColorIndex));

        createColorButtons();
    }

    private void createColorButtons() {
        buttonContainer.removeAllViews(); // 기존 버튼 제거

        List<Integer> choices = new ArrayList<>();
        choices.add(currentColorIndex);

        // 다른 2개 색상 추가
        while (choices.size() < 3) {
            int randomIndex = random.nextInt(colorValues.size());
            if (!choices.contains(randomIndex)) {
                choices.add(randomIndex);
            }
        }
        Typeface font = ResourcesCompat.getFont(this, R.font.hakgyoansimr);
        Collections.shuffle(choices); // 랜덤 정렬
        for (int index : choices) {
            String colorName = colorNames.get(index);
            Button button = new Button(this);
            button.setText(colorName);
            button.setBackgroundColor(colorValues.get(index));
            button.setTypeface(font);
            button.setTextColor(Color.WHITE);
            button.setTextSize(20);
            button.setOnClickListener(v -> handleAnswer(colorName));
            buttonContainer.addView(button);
        }
    }

    private void handleAnswer(String selectedColor) {
        String correctAnswer = colorNames.get(currentColorIndex);
        if (selectedColor.equals(correctAnswer)) {
            score++;
            resultText.setText("정답입니다!");
        } else {
            resultText.setText("정답이 아닙니다. 정답은 \"" + correctAnswer + "\"입니다.");
        }
        scoreText.setText("현재 점수: " + score);

        // 다음 문제로 이동
        setNewQuestion();
    }

    private void showFinalResult() {
        colorText.setText("게임 종료!");
        colorText.setTextColor(Color.BLACK); // 텍스트 색상을 검정색으로 설정
        resultText.setText("총 " + totalQuestions + "문제 중 " + score + "문제를 맞췄습니다.");
        scoreText.setVisibility(View.GONE); // 현재 점수 숨기기
        buttonContainer.removeAllViews(); // 버튼 제거
        mainMenuButton.setVisibility(View.VISIBLE); // 돌아가기 버튼 표시
    }
}
