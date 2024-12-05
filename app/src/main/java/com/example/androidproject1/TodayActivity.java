package com.example.androidproject1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.inputmethod.InputMethodManager;

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
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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
                showResults();
            }
        });
    }

    private void addDefaultQuestions() {
        List<Question> defaultQuestions = List.of(
                new Question("오늘의 날짜는\n몇 월 몇 일인가요? 예시) 12월 1일", "", null),
                new Question("경찰서로 연결되는 번호는?", "112", List.of("119", "112", "114", "131")),
                new Question("응급상황시 병원에 연결하려면?", "119", List.of("119", "112", "114", "131")),
                new Question("1년은 며칠인가요?", "365일", List.of("362일", "365일", "366일", "364일")),
                new Question("1년은 몇 달로 되어있나요?", "12달", List.of("12달", "10달", "6달", "9달")),
                new Question("하루는 몇 시간일까요?", "24시간", List.of("24시간", "12시간", "16시간", "20시간")),
                new Question("한 시간은 몇 분일까요?", "60분", List.of("30분", "45분", "60분", "50분")),
                new Question("1분은 몇 초일까요?", "60초", List.of("60초", "30초", "45초", "90초"))
        );
        allQuestions.addAll(defaultQuestions);
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
        buttonContainer.removeAllViews(); // 기존 버튼 제거
        buttonContainer.setRowCount(2); // 2행
        buttonContainer.setColumnCount(2); // 2열
        selectedAnswerIndex = -1; // 선택된 답변 초기화
        List<Button> optionButtons = new ArrayList<>(); // 생성된 버튼 저장

        for (int i = 0; i < 4; i++) { // 보기 4개
            String option = options.get(i);
            final int index = i;

            // 버튼 생성
            Button button = new Button(this);
            button.setText(option);
            button.setTextSize(18); // 텍스트 크기
            button.setTypeface(ResourcesCompat.getFont(this, R.font.hakgyoansimb)); // 커스텀 폰트 설정
            button.setGravity(Gravity.CENTER);
            button.setTextColor(Color.WHITE);
            button.setTextSize(32);

            // 기본 배경 설정 (drawable 파일 사용)
            button.setBackgroundResource(R.drawable.roundbutton2); // 기본 버튼 배경

            // 버튼 레이아웃 설정
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 400;
            params.height = 400;
            params.columnSpec = GridLayout.spec(i % 2, 1f); // 열은 0 또는 1
            params.rowSpec = GridLayout.spec(i / 2, 1f); // 행은 0 또는 1
            params.setMargins(10, 10, 10, 10); // 버튼 간격
            button.setLayoutParams(params);

            // 버튼 클릭 이벤트
            button.setOnClickListener(v -> {
                // 모든 버튼의 배경을 기본으로 초기화
                for (Button btn : optionButtons) {
                    btn.setBackgroundResource(R.drawable.roundbutton2); // 기본 배경
                }
                // 선택된 버튼의 배경 변경
                button.setBackgroundResource(R.drawable.selectedbutton); // 선택된 상태의 배경
                selectedAnswerIndex = index; // 선택된 답변 저장
                nextBtn.setVisibility(View.VISIBLE); // 다음 버튼 표시
            });

            // 버튼을 컨테이너에 추가
            buttonContainer.addView(button);
            optionButtons.add(button);
        }

        // 초기에는 "다음" 버튼 숨김
        nextBtn.setVisibility(View.GONE);
    }

    private void processAnswer(EditText subjectiveAnswer) {
        Question currentQuestion = allQuestions.get(currentQuestionIndex);

        if (currentQuestion.getOptions() == null) { // 주관식
            String userAnswer = subjectiveAnswer.getText().toString().trim();

            if (currentQuestion.getQuestion().contains("오늘의 날짜는")) {
                Calendar calendar = Calendar.getInstance();
                String correctAnswer = (calendar.get(Calendar.MONTH) + 1) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일";
                if (!userAnswer.equals(correctAnswer)) {
                    incorrectAnswers.put(currentQuestionIndex, "정답: " + correctAnswer);
                } else {
                    score++;
                }
            } else {
                if (!userAnswer.equalsIgnoreCase(currentQuestion.getAnswer())) {
                    incorrectAnswers.put(currentQuestionIndex, "정답: " + currentQuestion.getAnswer());
                } else {
                    score++;
                }
            }
        } else { // 객관식
            if (selectedAnswerIndex != currentQuestion.getCorrectAnswerIndex()) {
                incorrectAnswers.put(currentQuestionIndex, "정답: " + currentQuestion.getOptions().get(currentQuestion.getCorrectAnswerIndex()));
            } else {
                score++;
            }
        }
    }

    private void showResults() {
        Intent intent = new Intent(TodayActivity.this, TodayresultActivity.class);
        intent.putExtra("score", score); // 점수 전달
        // 틀린 문제 전달
        Map<String, String> formattedIncorrectAnswers = new HashMap<>();
        for (Map.Entry<Integer, String> entry : incorrectAnswers.entrySet()) {
            String question = allQuestions.get(entry.getKey()).getQuestion();
            formattedIncorrectAnswers.put(question, entry.getValue());
        }
        intent.putExtra("incorrectAnswers", (HashMap<String, String>) formattedIncorrectAnswers);
        startActivity(intent); // 결과 화면으로 이동
        finish(); // 현재 Activity 종료
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메뉴를 Toolbar에 추가
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent intent = new Intent(TodayActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_custom) {
            Intent intent = new Intent(TodayActivity.this, CustomActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


