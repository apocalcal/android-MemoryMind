package com.example.androidproject1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class TodayresultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todayresult);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        TextView incorrectTextView = findViewById(R.id.incorrectTextView);
        TextView resultTextView = findViewById(R.id.resultTextView);

        // 점수 및 틀린 문제 정보 받기
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        Map<String, String> incorrectAnswers = (Map<String, String>) intent.getSerializableExtra("incorrectAnswers");
        // 점수 표시
        scoreTextView.setText("맞은 문제: " + score + "개");
        incorrectTextView.setText("틀린 문제: " + (incorrectAnswers != null ? incorrectAnswers.size() : 0) + "개");
        // 틀린 문제 표시
        if (incorrectAnswers != null && !incorrectAnswers.isEmpty()) {
            StringBuilder incorrectDetails = new StringBuilder();
            incorrectDetails.append("틀린 문제:\n\n");
            for (Map.Entry<String, String> entry : incorrectAnswers.entrySet()) {
                incorrectDetails.append("- ").append(entry.getKey()).append("\n");
                incorrectDetails.append("  ").append(entry.getValue()).append("\n\n");
            }
            resultTextView.setText(incorrectDetails.toString());
        } else {
            resultTextView.setText("모든 문제를 맞췄습니다!");
        }
    }
}
