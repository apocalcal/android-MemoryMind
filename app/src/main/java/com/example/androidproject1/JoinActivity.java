package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    private EditText inputName, inputBirth, inputId, inputPw;
    private Button checkBtn, doneBtn, loginBtn;
    private boolean isUsernameChecked = false;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        inputName = findViewById(R.id.inputName);
        inputBirth = findViewById(R.id.inputBirth);
        inputId = findViewById(R.id.inputId);
        inputPw = findViewById(R.id.inputPw);
        checkBtn = findViewById(R.id.checkBtn);
        doneBtn = findViewById(R.id.doneBtn);
        loginBtn = findViewById(R.id.loginBtn);
        calendar = Calendar.getInstance();

        checkBtn.setOnClickListener(v -> {
            String username = inputId.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(JoinActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }
            checkUsername(username);
        });

        inputBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        JoinActivity.this,
                        R.style.SpinnerDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // 선택한 날짜를 EditText에 표시
                                String birth = year + "-" + (month + 1) + "-" + dayOfMonth;
                                inputBirth.setText(birth);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUsernameChecked) {
                    Toast.makeText(JoinActivity.this, "아이디 중복 확인이 필요합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = inputName.getText().toString().trim();
                String birth = inputBirth.getText().toString().trim();
                String username = inputId.getText().toString().trim();
                String password = inputPw.getText().toString().trim();

                if (name.isEmpty() || birth.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(new User(name, birth, username, password));
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(User user) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.registerUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Retrofit", "Failure: " + t.getMessage());
                Toast.makeText(JoinActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUsername(String username) {
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<Void> call = apiService.checkUsername(new UsernameRequest(username));

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            isUsernameChecked = true;  // 중복 확인 완료
                            Toast.makeText(JoinActivity.this, "사용 가능한 아이디입니다", Toast.LENGTH_SHORT).show();
                        } else {
                            isUsernameChecked = false;
                            Toast.makeText(JoinActivity.this, "중복된 아이디입니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Retrofit", "Failure: " + t.getMessage());
                        Toast.makeText(JoinActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



