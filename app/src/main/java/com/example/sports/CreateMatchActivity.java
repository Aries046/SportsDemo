package com.example.sports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sports.adapter.PlayerAdapter;
import com.example.sports.data.MatchManager;
import com.example.sports.model.Match;
import com.example.sports.model.Player;
import com.example.sports.model.Team;

public class CreateMatchActivity extends AppCompatActivity {

    private EditText team1NameEditText;
    private EditText team2NameEditText;
    private Button addTeam1PlayerButton;
    private Button addTeam2PlayerButton;
    private Button createMatchButton;
    private RecyclerView team1PlayersRecyclerView;
    private RecyclerView team2PlayersRecyclerView;
    private RadioGroup setsRadioGroup;
    private RadioButton threeSetRadioButton;
    private RadioButton fiveSetRadioButton;

    private Team team1;
    private Team team2;
    private PlayerAdapter team1Adapter;
    private PlayerAdapter team2Adapter;
    private MatchManager matchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        // 获取MatchManager实例
        matchManager = MatchManager.getInstance();

        // 初始化视图
        initViews();

        // 设置RecyclerView
        setupRecyclerViews();

        // 设置点击事件
        setupClickListeners();
    }

    private void initViews() {
        team1NameEditText = findViewById(R.id.team1NameEditText);
        team2NameEditText = findViewById(R.id.team2NameEditText);
        addTeam1PlayerButton = findViewById(R.id.addTeam1PlayerButton);
        addTeam2PlayerButton = findViewById(R.id.addTeam2PlayerButton);
        createMatchButton = findViewById(R.id.createMatchButton);
        team1PlayersRecyclerView = findViewById(R.id.team1PlayersRecyclerView);
        team2PlayersRecyclerView = findViewById(R.id.team2PlayersRecyclerView);
        setsRadioGroup = findViewById(R.id.setsRadioGroup);
        threeSetRadioButton = findViewById(R.id.threeSetRadioButton);
        fiveSetRadioButton = findViewById(R.id.fiveSetRadioButton);
    }

    private void setupRecyclerViews() {
        // 创建两个临时队伍
        team1 = new Team("队伍1");
        team2 = new Team("队伍2");

        // 设置队伍1的RecyclerView
        team1Adapter = new PlayerAdapter(team1.getPlayers(), player -> removePlayer(player, team1));
        team1PlayersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        team1PlayersRecyclerView.setAdapter(team1Adapter);

        // 设置队伍2的RecyclerView
        team2Adapter = new PlayerAdapter(team2.getPlayers(), player -> removePlayer(player, team2));
        team2PlayersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        team2PlayersRecyclerView.setAdapter(team2Adapter);
    }

    private void setupClickListeners() {
        // 添加队伍1球员按钮
        addTeam1PlayerButton.setOnClickListener(v -> showAddPlayerDialog(team1));

        // 添加队伍2球员按钮
        addTeam2PlayerButton.setOnClickListener(v -> showAddPlayerDialog(team2));

        // 创建比赛按钮
        createMatchButton.setOnClickListener(v -> createMatch());
    }

    private void showAddPlayerDialog(Team team) {
        // 创建对话框布局
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_player, null);
        EditText playerNameEditText = dialogView.findViewById(R.id.playerNameEditText);

        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("添加", (dialog, which) -> {
                    String playerName = playerNameEditText.getText().toString().trim();
                    if (!playerName.isEmpty()) {
                        addPlayerToTeam(playerName, team);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void addPlayerToTeam(String playerName, Team team) {
        Player player = new Player(playerName, team);
        team.addPlayer(player);

        // 更新适配器
        if (team == team1) {
            team1Adapter.notifyDataSetChanged();
        } else if (team == team2) {
            team2Adapter.notifyDataSetChanged();
        }
    }

    private void removePlayer(Player player, Team team) {
        team.removePlayer(player);

        // 更新适配器
        if (team == team1) {
            team1Adapter.notifyDataSetChanged();
        } else if (team == team2) {
            team2Adapter.notifyDataSetChanged();
        }
    }

    private void createMatch() {
        // 获取队伍名称
        String team1Name = team1NameEditText.getText().toString().trim();
        String team2Name = team2NameEditText.getText().toString().trim();

        // 检查队伍名称
        if (team1Name.isEmpty() || team2Name.isEmpty()) {
            Toast.makeText(this, "请输入队伍名称", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查队伍球员数量
        if (team1.getPlayers().size() < 2 || team2.getPlayers().size() < 2) {
            Toast.makeText(this, "每支队伍至少需要两名球员", Toast.LENGTH_SHORT).show();
            return;
        }

        // 设置队伍名称
        team1.setName(team1Name);
        team2.setName(team2Name);

        // 创建比赛
        Match match = matchManager.createMatch(team1, team2);

        // 启动比赛活动
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
        finish();
    }
}