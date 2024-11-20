package com.example.androidproject1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColormatchActivity extends AppCompatActivity {

    private TextView colorText, scoreText, levelText;
    private Button buttonRed, buttonBlue, buttonGreen, mainMenuButton;
    private List<String> colorNames;
    private List<Integer> colorValues;
    private int currentColorIndex;
    private Random random;
    private int score = 0;
    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatch);

        // View 초기화
        colorText = findViewById(R.id.colorText);
        scoreText = findViewById(R.id.scoreText);
        levelText = findViewById(R.id.levelText);
        buttonRed = findViewById(R.id.buttonRed);
        buttonBlue = findViewById(R.id.buttonBlue);
        buttonGreen = findViewById(R.id.buttonGreen);
        mainMenuButton = findViewById(R.id.mainMenuButton);

        // 초기 색상 리스트
        colorNames = new ArrayList<>();
        colorValues = new ArrayList<>();
        addInitialColors();

        random = new Random();

        // 첫 색상 설정
        setNewColor();

        // 버튼 클릭 이벤트 설정
        buttonRed.setOnClickListener(v -> handleAnswer("Red"));
        buttonBlue.setOnClickListener(v -> handleAnswer("Blue"));
        buttonGreen.setOnClickListener(v -> handleAnswer("Green"));

        // 메인 메뉴 버튼 이벤트
        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ColormatchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // 초기 색상 추가
    private void addInitialColors() {
        colorNames.add("Red");
        colorValues.add(Color.RED);
        colorNames.add("Blue");
        colorValues.add(Color.BLUE);
        colorNames.add("Green");
        colorValues.add(Color.GREEN);
    }

    // 새로운 색상 설정
    private void setNewColor() {
        int textIndex = random.nextInt(colorNames.size());
        currentColorIndex = random.nextInt(colorValues.size());

        colorText.setText(colorNames.get(textIndex));
        colorText.setTextColor(colorValues.get(currentColorIndex));
    }

    // 정답 처리
    private void handleAnswer(String selectedColor) {
        if (colorNames.get(currentColorIndex).equals(selectedColor)) {
            score++;
            scoreText.setText("Score: " + score);
            if (score % 3 == 0) {
                levelUp();
            }
            setNewColor();
        } else {
            gameOver();
        }
    }

    // 레벨 업
    private void levelUp() {
        level++;
        levelText.setText("Level: " + level);

        // 새 색상 즉시 추가
        switch (level) {
            case 2:
                if (!colorNames.contains("Yellow")) {
                    colorNames.add("Yellow");
                    colorValues.add(Color.YELLOW);
                }
                break;
            case 3:
                if (!colorNames.contains("Cyan")) {
                    colorNames.add("Cyan");
                    colorValues.add(Color.CYAN);
                }
                break;
            case 4:
                if (!colorNames.contains("Magenta")) {
                    colorNames.add("Magenta");
                    colorValues.add(Color.MAGENTA);
                }
                break;
            default:
                break;
        }

        Toast.makeText(this, "Level Up! Now Level " + level, Toast.LENGTH_SHORT).show();
    }

    // 게임 종료 처리
    private void gameOver() {
        Toast.makeText(this, "Game Over! Final Score: " + score, Toast.LENGTH_SHORT).show();
        score = 0;
        level = 1;
        scoreText.setText("Score: " + score);
        levelText.setText("Level: " + level);

        // 초기 색상으로 복구
        colorNames.clear();
        colorValues.clear();
        addInitialColors();

        // 메인 메뉴 버튼 활성화
        mainMenuButton.setVisibility(View.VISIBLE);
    }
}

