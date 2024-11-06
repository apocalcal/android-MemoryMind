package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidproject1.R;
import com.example.androidproject1.loginActivity;
import com.example.androidproject1.joinActivity;

public class MainActivity extends AppCompatActivity {
    private Button todayQuestBtn, selfCheckBtn, myTodayBtn, cognitveBtn, loginBtn, joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 레이아웃 요소 참조
        todayQuestBtn = findViewById(R.id.todayQuestBtn);
        selfCheckBtn = findViewById(R.id.selfCheckBtn);
        myTodayBtn = findViewById(R.id.myTodayBtn);
        cognitveBtn = findViewById(R.id.cognitveBtn);
        cognitveBtn = findViewById(R.id.cognitveBtn);
        loginBtn = findViewById(R.id.loginBtn);
        joinBtn = findViewById(R.id.joinBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, joinActivity.class);
                startActivity(intent);
            }
        });
    }
}
