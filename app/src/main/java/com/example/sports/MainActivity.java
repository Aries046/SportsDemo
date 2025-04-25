package com.example.sports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button createMatchButton;
    private Button viewMatchesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化视图
        initViews();

        // 设置点击事件
        setupClickListeners();
    }

    private void initViews() {
        createMatchButton = findViewById(R.id.createMatchButton);
        viewMatchesButton = findViewById(R.id.viewMatchesButton);
    }

    private void setupClickListeners() {
        // 创建比赛按钮
        createMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateMatchActivity.class);
                startActivity(intent);
            }
        });

        // 查看比赛记录按钮
        viewMatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 实现查看比赛记录功能
                // Intent intent = new Intent(MainActivity.this, MatchHistoryActivity.class);
                // startActivity(intent);
            }
        });
    }
}