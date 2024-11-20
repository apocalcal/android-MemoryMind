package com.example.androidproject1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomActivity extends AppCompatActivity {

    private EditText questionEdit, answerEdit;
    private Button saveBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        questionEdit = findViewById(R.id.questionEdit);
        answerEdit = findViewById(R.id.answerEdit);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEdit.getText().toString().trim();
                String answer = answerEdit.getText().toString().trim();

                if (question.isEmpty() || answer.isEmpty()) {
                    Toast.makeText(CustomActivity.this, "질문과 답변을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 유저 ID는 SharedPreferences에서 가져옴 (로그인 상태라 가정)
                int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);

                if (userId == -1) {
                    Toast.makeText(CustomActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveQuestion(userId, question, answer);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 액티비티 종료
            }
        });
    }

    private void saveQuestion(int userId, String question, String answer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.createQuestion(new QuestionRequest(userId, question, answer));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CustomActivity.this, "질문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 액티비티 종료
                } else {
                    Toast.makeText(CustomActivity.this, "저장 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CustomActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
