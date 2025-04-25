package com.example.sports;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sports.adapter.ActionRecordAdapter;
import com.example.sports.data.MatchManager;
import com.example.sports.model.Action;
import com.example.sports.model.Match;
import com.example.sports.model.Player;
import com.example.sports.model.Team;

import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    private TextView matchInfoTextView;
    private TextView scoreTextView;
    private TextView timerTextView;
    private RadioGroup teamRadioGroup;
    private RadioButton team1RadioButton;
    private RadioButton team2RadioButton;
    private Spinner playerSpinner;
    private Button serveButton;
    private Button receiveButton;
    private Button forehandButton;
    private Button backhandButton;
    private Button netApproachButton;
    private Button scoreButton;
    private RecyclerView actionRecordsRecyclerView;
    private Button endMatchButton;

    private MatchManager matchManager;
    private Match currentMatch;
    private Team selectedTeam;
    private Player selectedPlayer;
    private ActionRecordAdapter actionRecordAdapter;
    private Handler timerHandler;
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        // 获取MatchManager实例
        matchManager = MatchManager.getInstance();
        currentMatch = matchManager.getCurrentMatch();

        // 初始化视图
        initViews();

        // 设置队伍选择
        setupTeamSelection();

        // 设置球员选择
        setupPlayerSelection();

        // 设置动作按钮
        setupActionButtons();

        // 设置动作记录列表
        setupActionRecordsList();

        // 开始比赛
        startMatch();

        // 设置计时器
        setupTimer();
    }

    private void initViews() {
        matchInfoTextView = findViewById(R.id.matchInfoTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        teamRadioGroup = findViewById(R.id.teamRadioGroup);
        team1RadioButton = findViewById(R.id.team1RadioButton);
        team2RadioButton = findViewById(R.id.team2RadioButton);
        playerSpinner = findViewById(R.id.playerSpinner);
        serveButton = findViewById(R.id.serveButton);
        receiveButton = findViewById(R.id.receiveButton);
        forehandButton = findViewById(R.id.forehandButton);
        backhandButton = findViewById(R.id.backhandButton);
        netApproachButton = findViewById(R.id.netApproachButton);
        scoreButton = findViewById(R.id.scoreButton);
        actionRecordsRecyclerView = findViewById(R.id.actionRecordsRecyclerView);
        endMatchButton = findViewById(R.id.endMatchButton);
    }

    private void setupTeamSelection() {
        // 设置队伍名称
        team1RadioButton.setText(currentMatch.getTeam1().getName());
        team2RadioButton.setText(currentMatch.getTeam2().getName());

        // 默认选择队伍1
        selectedTeam = currentMatch.getTeam1();
        team1RadioButton.setChecked(true);

        // 设置队伍选择监听
        teamRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.team1RadioButton) {
                selectedTeam = currentMatch.getTeam1();
            } else if (checkedId == R.id.team2RadioButton) {
                selectedTeam = currentMatch.getTeam2();
            }
            // 更新球员下拉列表
            updatePlayerSpinner();
        });
    }

    private void setupPlayerSelection() {
        // 初始化球员下拉列表
        updatePlayerSpinner();

        // 设置球员选择监听
        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < selectedTeam.getPlayers().size()) {
                    selectedPlayer = selectedTeam.getPlayers().get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPlayer = null;
            }
        });
    }

    private void updatePlayerSpinner() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : selectedTeam.getPlayers()) {
            playerNames.add(player.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, playerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(adapter);

        // 默认选择第一个球员
        if (!selectedTeam.getPlayers().isEmpty()) {
            selectedPlayer = selectedTeam.getPlayers().get(0);
        }
    }

    private void setupActionButtons() {
        // 发球按钮
        serveButton.setOnClickListener(v -> recordAction(Action.SERVE));

        // 接发球按钮
        receiveButton.setOnClickListener(v -> recordAction(Action.RECEIVE));

        // 正手击球按钮
        forehandButton.setOnClickListener(v -> recordAction(Action.FOREHAND));

        // 反手击球按钮
        backhandButton.setOnClickListener(v -> recordAction(Action.BACKHAND));

        // 上网按钮
        netApproachButton.setOnClickListener(v -> recordAction(Action.NET_APPROACH));

        // 得分按钮
        scoreButton.setOnClickListener(v -> recordAction(Action.SCORE));

        // 结束比赛按钮
        endMatchButton.setOnClickListener(v -> endMatch());
    }

    private void setupActionRecordsList() {
        actionRecordAdapter = new ActionRecordAdapter(currentMatch.getActionRecords());
        actionRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        actionRecordsRecyclerView.setAdapter(actionRecordAdapter);
    }

    private void startMatch() {
        try {
            currentMatch.start();
            updateMatchInfo();
            updateScore();
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void recordAction(Action action) {
        if (selectedPlayer == null) {
            Toast.makeText(this, "请先选择球员", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            currentMatch.recordAction(selectedPlayer, action);
            actionRecordAdapter.notifyDataSetChanged();
            updateScore();

            // 滚动到最后一条记录
            if (currentMatch.getActionRecords().size() > 0) {
                actionRecordsRecyclerView.smoothScrollToPosition(currentMatch.getActionRecords().size() - 1);
            }
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void endMatch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("结束比赛")
                .setMessage("确定要结束当前比赛吗？")
                .setPositiveButton("结束", (dialog, which) -> {
                    currentMatch.finish();
                    updateMatchInfo();
                    disableActions();
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void updateMatchInfo() {
        if (currentMatch.isFinished()) {
            matchInfoTextView.setText("比赛已结束");
        } else {
            matchInfoTextView.setText(String.format("第%d局 进行中", currentMatch.getCurrentSet()));
        }
    }

    private void updateScore() {
        scoreTextView.setText(currentMatch.getScoreDisplay());
    }

    private void disableActions() {
        serveButton.setEnabled(false);
        receiveButton.setEnabled(false);
        forehandButton.setEnabled(false);
        backhandButton.setEnabled(false);
        netApproachButton.setEnabled(false);
        scoreButton.setEnabled(false);
        teamRadioGroup.setEnabled(false);
        playerSpinner.setEnabled(false);
        endMatchButton.setEnabled(false);
    }

    private void setupTimer() {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = currentMatch.getCurrentMatchTime();
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerRunnable, 0);
    }
}