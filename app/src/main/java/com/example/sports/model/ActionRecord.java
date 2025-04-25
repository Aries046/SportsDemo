package com.example.sports.model;

public class ActionRecord {
    private Player player;
    private Action action;
    private long timestamp; // 从比赛开始算起的毫秒数

    public ActionRecord(Player player, Action action, long timestamp) {
        this.player = player;
        this.action = action;
        this.timestamp = timestamp;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // 格式化时间戳为mm:ss格式
    public String getFormattedTime() {
        long seconds = timestamp / 1000;
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}