package com.example.androidproject1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class CognitiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognitive);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 글자 색깔 맞추기 게임 버튼
        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(CognitiveActivity.this, ColormatchActivity.class);
            startActivity(intent);
        });

        // 표지판 맞추기 게임 버튼
        Button startSignGameButton = findViewById(R.id.startSignGameButton);
        startSignGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(CognitiveActivity.this, SignMatchActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cognitive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent intent = new Intent(CognitiveActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
