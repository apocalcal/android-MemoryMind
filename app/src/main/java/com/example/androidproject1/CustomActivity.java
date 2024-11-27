package com.example.androidproject1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomActivity extends AppCompatActivity {

    private EditText questionEdit, answerEdit, option1, option2, option3, option4;
    private Button saveBtn, backBtn;
    private RadioGroup questionTypeGroup;
    private LinearLayout objectiveOptionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        questionEdit = findViewById(R.id.questionEdit);
        answerEdit = findViewById(R.id.answerEdit);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);
        questionTypeGroup = findViewById(R.id.questionTypeGroup);
        objectiveOptionsLayout = findViewById(R.id.objectiveOptionsLayout);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        // 라디오 버튼 선택 이벤트 처리
        questionTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioSubjective) {
                    objectiveOptionsLayout.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioObjective) {
                    objectiveOptionsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEdit.getText().toString().trim();
                String answer = answerEdit.getText().toString().trim();

                if (questionTypeGroup.getCheckedRadioButtonId() == R.id.radioSubjective) {
                    saveQuestion(question, answer, null);
                } else {
                    String opt1 = option1.getText().toString().trim();
                    String opt2 = option2.getText().toString().trim();
                    String opt3 = option3.getText().toString().trim();
                    String opt4 = option4.getText().toString().trim();

                    if (opt1.isEmpty() || opt2.isEmpty() || opt3.isEmpty() || opt4.isEmpty()) {
                        Toast.makeText(CustomActivity.this, "모든 옵션을 입력하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    saveQuestion(question, answer, new String[]{opt1, opt2, opt3, opt4});
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveQuestion(String question, String answer, String[] options) {
        // ApiService에서 options 처리 가능하도록 수정
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(CustomActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // QuestionRequest에 userId 포함
        QuestionRequest questionRequest = new QuestionRequest(userId, question, answer, options);

        Call<Void> call = apiService.createQuestion(questionRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CustomActivity.this, "질문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
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
