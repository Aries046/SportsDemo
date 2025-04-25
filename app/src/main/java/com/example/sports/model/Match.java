package com.example.sports.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Match {
    private String id;
    private Team team1;
    private Team team2;
    private boolean isStarted;
    private boolean isFinished;
    private List<ActionRecord> actionRecords;
    private long startTime;
    private Player lastActionPlayer; // 记录最后一个动作的球员
    private Action lastAction; // 记录最后一个动作
    private int winningScore = 11; // 默认获胜分数
    private int maxSets = 3; // 默认最大局数
    private List<SetResult> setResults; // 每局的结果

    public Match(Team team1, Team team2) {
        this.id = UUID.randomUUID().toString();
        this.team1 = team1;
        this.team2 = team2;
        this.isStarted = false;
        this.isFinished = false;
        this.actionRecords = new ArrayList<>();
        this.setResults = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<ActionRecord> getActionRecords() {
        return actionRecords;
    }

    // 获取每局比赛结果
    public List<SetResult> getSetResults() {
        return setResults;
    }

    // 设置最大局数
    public void setMaxSets(int maxSets) {
        this.maxSets = maxSets;
    }

    // 获取最大局数
    public int getMaxSets() {
        return maxSets;
    }

    public void start() {
        if (team1.getPlayers().size() < 2 || team2.getPlayers().size() < 2) {
            throw new IllegalStateException("每支队伍至少需要两名球员才能开始比赛");
        }
        this.isStarted = true;
        this.startTime = System.currentTimeMillis();
    }

    public void finish() {
        this.isFinished = true;
        // 保存当前局的结果
        saveCurrentSetResult();
    }

    // 记录动作
    public void recordAction(Player player, Action action) {
        if (!isStarted) {
            throw new IllegalStateException("比赛尚未开始");
        }
        if (isFinished) {
            throw new IllegalStateException("比赛已经结束");
        }

        // 检查游戏逻辑
        validateAction(player, action);

        long timestamp = System.currentTimeMillis() - startTime;
        ActionRecord record = new ActionRecord(player, action, timestamp);
        actionRecords.add(record);

        lastActionPlayer = player;
        lastAction = action;

        // 如果是得分动作，增加相应队伍的分数
        if (action == Action.SCORE) {
            player.getTeam().increaseScore();

            // 检查是否达到获胜条件
            if (checkSetFinished()) {
                saveCurrentSetResult();
                resetScores();

                // 检查比赛是否结束
                if (checkMatchFinished()) {
                    finish();
                }
            }
        }
    }

    // 验证动作是否合法
    private void validateAction(Player player, Action action) {
        // 如果是第一个动作，必须是发球
        if (lastAction == null && action != Action.SERVE) {
            throw new IllegalStateException("比赛必须以发球开始");
        }

        // 只有在球员刚发完球之后，才可以记录"得分"
        if (action == Action.SCORE) {
            if (lastAction != Action.SERVE && lastAction != Action.RECEIVE &&
                lastAction != Action.FOREHAND && lastAction != Action.BACKHAND &&
                lastAction != Action.NET_APPROACH) {
                throw new IllegalStateException("只有在有效击球后才能记录得分");
            }
        }

        // 只有在接发球后才能继续下一回合
        if (lastAction == Action.SERVE && action != Action.RECEIVE && action != Action.SCORE) {
            throw new IllegalStateException("发球后必须是接发球或得分");
        }
    }

    // 检查当前局是否结束
    private boolean checkSetFinished() {
        int team1Score = team1.getScore();
        int team2Score = team2.getScore();
        return (team1Score >= winningScore && team1Score - team2Score >= 2) ||
               (team2Score >= winningScore && team2Score - team1Score >= 2);
    }

    // 保存当前局结果
    private void saveCurrentSetResult() {
        SetResult result = new SetResult(team1.getScore(), team2.getScore());
        setResults.add(result);
    }

    // 重置分数开始新局
    private void resetScores() {
        team1.resetScore();
        team2.resetScore();
    }

    // 检查整场比赛是否结束
    private boolean checkMatchFinished() {
        int team1Sets = 0;
        int team2Sets = 0;

        for (SetResult result : setResults) {
            if (result.getTeam1Score() > result.getTeam2Score()) {
                team1Sets++;
            } else {
                team2Sets++;
            }
        }

        int requiredSets = (maxSets / 2) + 1; // 例如5局3胜
        return team1Sets >= requiredSets || team2Sets >= requiredSets;
    }

    // 获取当前分数显示
    public String getScoreDisplay() {
        return String.format("%s: %d | %s: %d",
                            team1.getName(), team1.getScore(),
                            team2.getName(), team2.getScore());
    }

    // 获取当前局数
    public int getCurrentSet() {
        return setResults.size() + 1;
    }

    // 获取当前最后动作的时间
    public long getCurrentMatchTime() {
        if (!isStarted) {
            return 0;
        }
        return System.currentTimeMillis() - startTime;
    }

    // 获取比赛获胜者队伍
    public Team getWinner() {
        if (!isFinished) {
            return null;
        }

        int team1Sets = 0;
        int team2Sets = 0;

        for (SetResult result : setResults) {
            if (result.getTeam1Score() > result.getTeam2Score()) {
                team1Sets++;
            } else {
                team2Sets++;
            }
        }

        if (team1Sets > team2Sets) {
            return team1;
        } else {
            return team2;
        }
    }

    // 内部类，表示每局比赛的结果
    public static class SetResult {
        private int team1Score;
        private int team2Score;

        public SetResult(int team1Score, int team2Score) {
            this.team1Score = team1Score;
            this.team2Score = team2Score;
        }

        public int getTeam1Score() {
            return team1Score;
        }

        public int getTeam2Score() {
            return team2Score;
        }
    }
}