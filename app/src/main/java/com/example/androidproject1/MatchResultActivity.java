package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MatchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);

        TextView resultText = findViewById(R.id.resultText);
        Button backButton = findViewById(R.id.backButton); // 수정된 ID로 버튼 참조

        try {
            // Intent로 전달된 데이터 받기
            int score = getIntent().getIntExtra("SCORE", -1); // 기본값 -1
            int totalSigns = getIntent().getIntExtra("TOTAL_SIGNS", -1); // 기본값 -1

            // 데이터 검증
            if (score < 0 || totalSigns < 0) {
                resultText.setText("결과 데이터를 불러오는 데 문제가 발생했습니다.");
                Log.e("MatchResultActivity", "잘못된 결과 데이터: SCORE=" + score + ", TOTAL_SIGNS=" + totalSigns);
            } else {
                resultText.setText("맞춘 표지판: " + score + " / " + totalSigns);
                Log.d("MatchResultActivity", "결과 표시: SCORE=" + score + ", TOTAL_SIGNS=" + totalSigns);
            }
        } catch (Exception e) {
            resultText.setText("결과를 불러오는 중 오류가 발생했습니다.");
            Log.e("MatchResultActivity", "결과 처리 중 오류 발생", e);
        }

        // 뒤로가기 버튼 동작
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MatchResultActivity", "뒤로가기 버튼 클릭됨");
                Intent intent = new Intent(MatchResultActivity.this, CognitiveActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }
        });
    }
}
