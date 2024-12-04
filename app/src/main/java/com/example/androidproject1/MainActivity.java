package com.example.androidproject1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject1.R;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private TextView loginSuggest, todayText, customText, selfText, cognitiveText;
    private ImageView icon;
    private Button todayQuestBtn, selfCheckBtn, cognitveBtn, loginBtn, joinBtn, customBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 로그인 상태 확인
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String username = sharedPreferences.getString("username", "");  // 저장된 아이디 가져오기
        setContentView(R.layout.activity_main);
        // 레이아웃 요소 참조
        todayQuestBtn = findViewById(R.id.todayQuestBtn);
        selfCheckBtn = findViewById(R.id.selfCheckBtn);
        cognitveBtn = findViewById(R.id.cognitveBtn);
        loginBtn = findViewById(R.id.loginBtn);
        joinBtn = findViewById(R.id.joinBtn);
        customBtn = findViewById(R.id.customBtn);
        loginSuggest = findViewById(R.id.loginSuggest);
        todayText = findViewById(R.id.todayText);
        customText = findViewById(R.id.customText);
        selfText = findViewById(R.id.selfText);
        cognitiveText = findViewById(R.id.cognitiveText);
        icon = findViewById(R.id.icon);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (isLoggedIn) {
            loginSuggest.setText("환영합니다, " + username + "님!");
            joinBtn.setVisibility(View.GONE);
            icon.setVisibility(View.GONE);
            todayQuestBtn.setVisibility(View.VISIBLE);
            selfCheckBtn.setVisibility(View.VISIBLE);
            customBtn.setVisibility(View.VISIBLE);
            cognitveBtn.setVisibility(View.VISIBLE);
            todayText.setVisibility(View.VISIBLE);
            customText.setVisibility(View.VISIBLE);
            selfText.setVisibility(View.VISIBLE);
            cognitiveText.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
            joinBtn.setVisibility(View.GONE);
        } else {
            loginSuggest.setText("");
            icon.setVisibility(View.VISIBLE);
            todayQuestBtn.setVisibility(View.GONE);
            selfCheckBtn.setVisibility(View.GONE);
            customBtn.setVisibility(View.GONE);
            cognitveBtn.setVisibility(View.GONE);
            todayText.setVisibility(View.GONE);
            customText.setVisibility(View.GONE);
            selfText.setVisibility(View.GONE);
            cognitiveText.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
        }
        todayQuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodayActivity.class);
                startActivity(intent);
            }
        });

        selfCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelfcheckActivity.class);
                startActivity(intent);
            }
        });

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                startActivity(intent);
            }
        });

        cognitveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CognitiveActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메뉴를 Toolbar에 추가
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("username");
            editor.remove("user_id");
            editor.apply();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

