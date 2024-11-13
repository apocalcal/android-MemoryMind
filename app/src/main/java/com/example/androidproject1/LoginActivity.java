package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editId, editPw;
    private Button backBtn, joinBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editId = findViewById(R.id.editId);
        editPw = findViewById(R.id.editPw);
        loginBtn = findViewById(R.id.loginBtn);
//        backBtn = findViewById(R.id.backBtn);
        joinBtn = findViewById(R.id.joinBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editId.getText().toString().trim();
                String password = editPw.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(new LoginRequest(username, password));
                }
            }
        });

//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
    }
    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", isLoggedIn);
        editor.apply();
    }

    private void loginUser(LoginRequest loginRequest) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    saveLoginState(true);
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", loginRequest.getUsername());  // 사용자 아이디 저장
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    // 로그인 성공 시 메인 화면으로 이동 등의 추가 작업 수행
                } else {
                    try {
                        String errorMessage = response.errorBody().string(); // 서버에서 받은 오류 메시지
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Retrofit", "Failure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

//
