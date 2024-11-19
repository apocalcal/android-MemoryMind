package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 결과 메시지 표시
        TextView resultTextView = findViewById(R.id.resultTextView);
        String resultMessage = getIntent().getStringExtra("RESULT_MESSAGE");
        resultTextView.setText(resultMessage != null ? resultMessage : "결과를 확인할 수 없습니다.");

        // "뒤로 가기" 버튼 처리
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            navigateToMainActivity();
        });

        // 하드웨어 뒤로 가기 버튼 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // 현재 Activity 종료
    }
}
