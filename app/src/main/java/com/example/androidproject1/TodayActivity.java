package com.example.androidproject1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayActivity extends AppCompatActivity {
    private List<Question> allQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedAnswerIndex = -1; // 객관식 선택된 답변 인덱스
    private Map<Integer, String> incorrectAnswers = new HashMap<>(); // 틀린 문제 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        TextView questionText = findViewById(R.id.questionText);
        GridLayout buttonContainer = findViewById(R.id.buttonContainer);
        EditText subjectiveAnswer = findViewById(R.id.subjectiveAnswer); // 주관식 답변
        Button nextBtn = findViewById(R.id.nextBtn);

        // 기본 질문 추가
        addDefaultQuestions();

        // 질문 데이터 로드
        loadQuestions(questionText, buttonContainer, subjectiveAnswer, nextBtn);

        nextBtn.setOnClickListener(v -> {
            if (currentQuestionIndex < allQuestions.size() - 1) {
                processAnswer(subjectiveAnswer);
                currentQuestionIndex++;
                updateQuestion(questionText, buttonContainer, subjectiveAnswer, nextBtn);
            } else {
                processAnswer(subjectiveAnswer);
                showResults(questionText, buttonContainer, subjectiveAnswer, nextBtn);
            }
        });
    }

    private void addDefaultQuestions() {
        // 기본 질문: 오늘 날짜 질문 추가
        allQuestions.add(new Question(
                "오늘의 날짜는 몇 월 몇 일인가요? (예시 : 2024년 10월 12일)",
                "", // 정답은 동적으로 확인하므로 비워둠
                null // 주관식이므로 options는 null
        ));
    }

    private void loadQuestions(TextView questionText, GridLayout buttonContainer, EditText subjectiveAnswer, Button nextBtn) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);

        apiService.getAllQuestions(userId).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allQuestions.addAll(response.body());
                    updateQuestion(questionText, buttonContainer, subjectiveAnswer, nextBtn);
                } else {
                    Toast.makeText(TodayActivity.this, "질문 데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(TodayActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuestion(TextView questionText, GridLayout buttonContainer, EditText subjectiveAnswer, Button nextBtn) {
        Question currentQuestion = allQuestions.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestion());

        if (currentQuestion.getOptions() == null) { // 주관식
            buttonContainer.setVisibility(View.GONE);
            subjectiveAnswer.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        } else { // 객관식
            buttonContainer.setVisibility(View.VISIBLE);
            subjectiveAnswer.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            createOptionButtons(buttonContainer, currentQuestion.getOptions(), nextBtn);
        }
    }

    private void createOptionButtons(GridLayout buttonContainer, List<String> options, Button nextBtn) {
        buttonContainer.removeAllViews();
        selectedAnswerIndex = -1;
        List<Button> optionButtons = new ArrayList<>();
        int buttonSize = 920 / 2 - 10;

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            final int index = i;
            Button button = new Button(this);
            button.setText(option);
            button.setTextSize(28);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(Color.WHITE);
            Typeface font = ResourcesCompat.getFont(this, R.font.hakgyoansimb);
            button.setTypeface(font);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = buttonSize; // 너비를 설정
            params.height = buttonSize; // 높이를 설정
            params.setMargins(10, 10, 10, 10);
            button.setLayoutParams(params);
            button.setOnClickListener(v -> {
                for (Button btn : optionButtons) {
                    btn.setBackgroundColor(Color.GRAY);
                }
                button.setBackgroundColor(Color.parseColor("#0C3155"));
                selectedAnswerIndex = index;
                nextBtn.setVisibility(View.VISIBLE);
            });

            button.setBackgroundColor(Color.GRAY);
            buttonContainer.addView(button);
            optionButtons.add(button);
        }
    }

    private void processAnswer(EditText subjectiveAnswer) {
        Question currentQuestion = allQuestions.get(currentQuestionIndex);

        if (currentQuestion.getOptions() == null) { // 주관식
            String userAnswer = subjectiveAnswer.getText().toString().trim();

            if (currentQuestion.getQuestion().contains("오늘의 날짜는")) {
                // 오늘의 날짜 질문 처리
                Calendar calendar = Calendar.getInstance();
                String correctAnswer = (calendar.get(Calendar.MONTH) + 1) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일";
                if (!userAnswer.equals(correctAnswer)) {
                    incorrectAnswers.put(currentQuestionIndex, "정답: " + correctAnswer + ", 입력: " + userAnswer);
                } else {
                    score++;
                }
            } else {
                if (!userAnswer.equalsIgnoreCase(currentQuestion.getAnswer())) {
                    incorrectAnswers.put(currentQuestionIndex, "정답: " + currentQuestion.getAnswer() + ", 입력: " + userAnswer);
                } else {
                    score++;
                }
            }
        } else { // 객관식
            if (selectedAnswerIndex != currentQuestion.getCorrectAnswerIndex()) {
                incorrectAnswers.put(currentQuestionIndex, "정답: " + currentQuestion.getOptions().get(currentQuestion.getCorrectAnswerIndex()) + ", 선택: " + currentQuestion.getOptions().get(selectedAnswerIndex));
            } else {
                score++;
            }
        }
    }

    private void showResults(TextView questionText, View buttonContainer, EditText subjectiveAnswer, Button nextBtn) {
        buttonContainer.setVisibility(View.GONE);
        subjectiveAnswer.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("퀴즈 완료!\n점수: ").append(score).append("/").append(allQuestions.size()).append("\n\n");

        if (!incorrectAnswers.isEmpty()) {
            resultMessage.append("틀린 문제:\n");
            for (Map.Entry<Integer, String> entry : incorrectAnswers.entrySet()) {
                resultMessage.append("- 질문 ").append(entry.getKey() + 1).append(": ").append(allQuestions.get(entry.getKey()).getQuestion()).append("\n");
                resultMessage.append("  ").append(entry.getValue()).append("\n");
            }
        } else {
            resultMessage.append("모든 문제를 맞췄습니다!");
        }

        questionText.setText(resultMessage.toString());
    }
}
